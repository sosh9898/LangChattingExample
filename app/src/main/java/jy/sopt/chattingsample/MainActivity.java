package jy.sopt.chattingsample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.room_name_edit)
    EditText nameEdit;
    @BindView(R.id.room_rcv)
    RecyclerView roomListRcv;

    List<Room> roomList;
    List<String> nop;
    DatabaseReference reference, user;
    String name, key;
    RoomListAdapter roomListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermission();
        SharedPreferencesService.getInstance().load(this);

        user = FirebaseDatabase.getInstance().getReference("user");

        reference = FirebaseDatabase.getInstance()
                .getReference("roomList");

        Query recentMessageTimeQuery = reference.orderByChild("recentMessageTime");

        setRecycler();

        if (SharedPreferencesService.getInstance().getPrefStringData("user").equals(""))
            createUserName();
        else {
            name = SharedPreferencesService.getInstance().getPrefStringData("user");
        }



        recentMessageTimeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomList.clear();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    roomList.add(dataSnapshot2.getValue(Room.class));
                }

                Log.i("room count", roomList.size() + "");

                Collections.sort(roomList, new Comparator<Room>() {
                    @Override
                    public int compare(Room room, Room t1) {
                        return t1.getRecentMessageTime().compareTo(room.getRecentMessageTime());
                    }
                });
                roomListAdapter.updateList(roomList);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int pos = roomListRcv.getChildAdapterPosition(view);

            //FirebaseMessaging.getInstance().subscribeToTopic(roomList.get(pos).getRoomName());

            reference.child(roomList.get(pos).getRoomName())
                    .child("numberOfPeople")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                            nop = new ArrayList<String>();

                            while (child.hasNext()) {

                                if (child.next().getValue().equals(SharedPreferencesService.getInstance().getPrefStringData("fcm_token"))) {
                                    return;
                                }
                            }
                            if(dataSnapshot.getValue() != null)
                                nop = (List<String>) dataSnapshot.getValue();

                            nop.add(SharedPreferencesService.getInstance().getPrefStringData("fcm_token"));

                            reference.child(roomList.get(pos).getRoomName()).child("numberOfPeople").setValue(nop);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("chat_room_name", roomList.get(pos).getRoomName());
            intent.putExtra("chat_user_name", name);
            startActivity(intent);
        }
    };

    public void setRecycler() {
        roomList = new ArrayList<>();
        roomListRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomListAdapter = new RoomListAdapter(roomList, onClickListener);
        roomListRcv.setAdapter(roomListAdapter);

    }

    @OnClick(R.id.create_room_btn)
    public void onClickCreateRoomBtn(View view) {
        Room roominfo = new Room(nameEdit.getText().toString(), name, null, null, 0, "", "");
        Map<String, Object> roomUpdate = new HashMap<String, Object>();
        roomUpdate.put(nameEdit.getText().toString(), roominfo);

        reference.updateChildren(roomUpdate);
    }


    private void createUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("채팅방에 사용할 이름을 입력하세요");

        final EditText builder_input = new EditText(this);

        builder.setView(builder_input);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = builder_input.getText().toString();
                SharedPreferencesService.getInstance().setPrefData("user", name);
                User tempUser = new User(name, SharedPreferencesService.getInstance().getPrefStringData("fcm_token"));
                Map<String, Object> userUpdate = new HashMap<String, Object>();
                userUpdate.put(user.push().getKey(), tempUser);

                user.updateChildren(userUpdate);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                createUserName();
            }
        });

        builder.show();
    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("LANG 을 100% 이용하기 위해 다음의 권한이 필요합니다!!")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
}
