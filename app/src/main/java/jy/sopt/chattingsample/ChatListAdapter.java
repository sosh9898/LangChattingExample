package jy.sopt.chattingsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jyoung on 2017. 12. 19..
 */

public class ChatListAdapter extends RecyclerView.Adapter {

    List<ChatDetail> chatDetailList;
    Context context;
    View.OnClickListener onClickListener;

    public static final int CHAT_TEXT = 101;
    public static final int CHAT_IMAGE = 102;
    public static final int CHAT_VIDEO = 103;
    public static final int CHAT_AUDIO = 104;
    public static final int CHAT_FILE = 105;

    public ChatListAdapter(List<ChatDetail> chatDetailList, Context context, View.OnClickListener onClickListener) {
        this.chatDetailList = chatDetailList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public void updateList(List<ChatDetail> chatDetailList){
        this.chatDetailList = chatDetailList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch(viewType){
            case CHAT_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_items, parent, false);
                return new ChatTextViewHolder(view);
            case CHAT_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_image_item, parent, false);
                view.setOnClickListener(onClickListener);
                return new ChatImageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case CHAT_TEXT: ((ChatTextViewHolder)holder).bindView(chatDetailList.get(position)); break;
            case CHAT_IMAGE: ((ChatImageViewHolder)holder).bindView(chatDetailList.get(position)); break;
        }
    }

    @Override
    public int getItemCount() {
        return chatDetailList != null ? chatDetailList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return chatDetailList.get(position).getMediaType();
    }

    class ChatTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.your_content)TextView youContent;
        @BindView(R.id.my_content)TextView meContent;
        @BindView(R.id.your_profile)ImageView youProfile;

        public ChatTextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            SharedPreferencesService.getInstance().load(context);
        }

        public void bindView(ChatDetail chatItem){

            if(chatItem.getSenderToken().equals(SharedPreferencesService.getInstance().getPrefStringData("fcm_token"))) {
                youProfile.setVisibility(View.INVISIBLE);
                meContent.setVisibility(View.VISIBLE);
                youContent.setVisibility(View.GONE);
                meContent.setText(chatItem.getContent());            }
            else{
                meContent.setVisibility(View.GONE);
                youContent.setVisibility(View.VISIBLE);
                youContent.setText(chatItem.getContent());
                if(getAdapterPosition() != 0) {
                    if (chatDetailList.get(getAdapterPosition() - 1).getSenderToken().equals(chatItem.getSenderToken()))
                        youProfile.setVisibility(View.INVISIBLE);

                    else
                        youProfile.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    class ChatImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_image)ImageView chatImage;

        public ChatImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(ChatDetail chatItem){
            Glide.with(chatImage.getContext())
                    .load(chatItem.getContent())
                    .centerCrop()
                    .into(chatImage);
        }
    }
}
