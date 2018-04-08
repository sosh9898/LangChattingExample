package jy.sopt.chattingsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jyoung on 2017. 12. 18..
 */

public class RoomListAdapter extends RecyclerView.Adapter {

    List<Room> roomList;
    View.OnClickListener onClickListener;

    public RoomListAdapter(List<Room> roomList, View.OnClickListener onClickListener) {
        this.roomList = roomList;
        this.onClickListener = onClickListener;
    }

    public void updateList(List<Room> roomList){
        this.roomList = roomList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_room_listitem, parent, false);
        view.setOnClickListener(onClickListener);
        return new RoomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RoomListViewHolder)holder).bindView(roomList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomList!=null?roomList.size():0;
    }

    class RoomListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.room_name)
        TextView roomName;

        public RoomListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Room roomItem){
            roomName.setText(roomItem.getRoomName());
        }
    }
}
