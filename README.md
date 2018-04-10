# LangChattingExample

언어교류 서비스 "Lang" 에 추가될 chatting 기능 Semi Project 입니다.

주 기능은 클라우드 서비스 Firebase를 활용하였으며, 다음과 같습니다.  **Realtime DB** ( 기본 채팅 ) , **Storage** ( 사진 전송 ), **Notification** ( 알림 기능 )



## # Firebase RealTime DB란?

> Firebase 실시간 데이터베이스는 **클라우드 호스팅 데이터베이스**입니다. 데이터는 JSON으로 저장되며 연결된 모든 클라이언트에 **실시간으로 동기화**됩니다. iOS, Android 및 자바스크립트 SDK로 교차 플랫폼 앱을 개발하면 모든 클라이언트가 하나의 실시간 데이터베이스 인스턴스를 공유하고 **자동 업데이트로 최신 데이터를 수신**합니다.



## # 주요 기능

* **다른 이용자와 그룹별, 1대1 채팅**

  * 채팅방 목록, 채팅방 내부에 해당하는 각 DB reference 에 요구 상황에 적합한 Listener 를 설정하여 효율적으로 데이터 변화에 대응하였습니다.
  * EventListener 정리는 아래있습니다.  ↓↓↓↓

* **Notification 서비스**

  * FCM 의 활용으로 채팅이 오고 가는 과정에서 사용자 기기에 알림 메시지를 전송합니다.

    ````java
    //subscribeToTopic 채팅방 이름(채널명)에 대해 수신하도록 설정
    FirebaseMessaging.getInstance().
    subscribeToTopic("채널명");

    //해당 채널명에 메시지 전송
    root.put("to", "/topics/" + "채널명");
    ````

* **읽지 않은 메시지 badge 효과**

  * 앱 아이콘 상단에 표시되는 전체 알림은 background 에서 FCM 을 수신할 경우 카운트하였습니다.
  * 각 채팅방의 읽지 않은 메시지 badge 는 SharedPreference 를 활용하여 마지막 채팅 시 채팅의 개수와 Realtime DB의 활용으로 추가된 데이터의 수를 알아내어 두 수의 차로 구현하였습니다.

* **Media 전송 / 저장 기능**

  * 전송 : Firebase Storage 를 이용, uploadtask 를 활용하여 사진을 전송하였습니다. 이때 반환되는 url을 Glide를 활용하여 ImageView 에 노출시켰습니다.
  * 저장 : downloadtask 를 활용하여 저장이 진행됩니다. 내부 저장소에 프로젝트 명으로 경로를 설정하고 local 파일을 만듭니다. 해당 local 파일에 사진을 저장하였습니다.



## # Firebase Realtime DB ( 데이터 읽기 3가지 )

* **addValueEventListener()**

  * 경로 전체 내용에 대한 변경 사항을 읽고 데이터 변화에 대해 대기합니다.

    ````java
    //해당 프로젝트 사용 예시
    private void setListenerForDateChanged(){
      Query recentMessageTimeQuery = reference.orderByChild("recentMessageTime");

      recentMessageTimeQuery.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              roomList.clear();
              for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                  roomList.add(dataSnapshot2.getValue(Room.class));
              }
              
              //Firebase sort 방식은 오름차순으로 제한하기에 내림차순으로 재정렬합니다.
              Collections.sort(roomList, new Comparator<Room>() {
                  @Override
                  public int compare(Room room, Room t1) {
                            return  t1.getRecentMessageTime()
                                .compareTo(room.getRecentMessageTime());
                        }
                  });
                  roomListAdapter.updateList(roomList);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }
    //다음과 같이 목록 전체를 읽고 recentMessageTime 순으로 재배치하는 경우 사용됩니다.
    ````

* **addListenerForSingleValueEvent()**

  * 한 번만 호출되고 다시 호출되지 않습니다.

  * 능동적으로 수신 대기할 필요가 없는 데이터에 활용됩니다.

    ````java
    //해당 프로젝트 사용 예시
    private void setListenerForUserList(){
     reference.child(roomList.get(pos).getRoomName())
                .child("numberOfPeople")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                nop = new ArrayList<String>();
    			Iterator<DataSnapshot> child =dataSnapshot.getChildren().iterator();
    			
                while (child.hasNext()){ if("기존 목록에 존재") return;}   
                
                if(dataSnapshot.getValue() != null)
                nop = (List<String>) dataSnapshot.getValue();
                  
                reference.child(roomList.get(pos)                                                                                                  			   .getRoomName()).child("numberOfPeople")
                .setValue(nop);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
    				Log.e(TAG, databaseError.toString());
                }
                });
    }
    // 채팅방 인원의 추가는 채팅방 목록 액티비티가 테스크에 존재하는 동안 한 번만 이루어지면 되기에 다음과 같이 사용됩니다.
    ````

* **addChildEventListener()**

  * 경로 전체가 아닌 하위 아이템의 데이터 변화에 대기합니다.

  * 특정 아이템에 대한 변경을 수신 대기하는데 활용됩니다.

    ````java
    //해당 프로젝트 사용 예시

    private void setListenerForDateChanged(){
    reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    chatConversation(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    chatConversation(dataSnapshot);
                }
          		//사용되지 않는 메소드는 표시하지 않았습니다.
            });
    }
    //다음은 채팅방 내부 입니다. 채팅방 내용 전체에 수신 대기하는 것은 불필요한 오버헤드가 발생하게 됩니다. 때문에 추가되는 하나의 채팅 object 에만 수신하도록 addChildEventListener()를 사용합니다.

    //chatConversation 메소드는 updateChildren 메소드를 통해 데이터를 추가합니다.
    ````



 

