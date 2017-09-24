
package com.example.bob.mynote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 2017/6/1.
 */
public class MyFragment extends Fragment {


    private static final String KEY_CONTENT = "com.example.bob.mynote.MyFragment.key_content";
    private View deleteButton;
    private boolean isDeleteShown = false;
    public static MyFragment newInstancce(String content,String mAccount) {
        MyFragment fragment = new MyFragment();
        Bundle arg = new Bundle();
        arg.putString(KEY_CONTENT, content);
        arg.putString("USER_ID", mAccount);
        fragment.setArguments(arg);
        return fragment;
    }

    private String mContent;
    public static RecyclerView recyclerView = null;
    public  static MyReyclerAdapter recycleAdapter;
    public static List<Data> mData = null;
    public static List<Data> mSearchList = null;
    private DB db;
    private String mAccount;
    private ViewGroup itemlayout;
    private int longPos = -1;
    LinearLayoutManager layoutManager;
    private EditText mSearchView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getArguments().getString(KEY_CONTENT);
        mAccount = getArguments().getString("USER_ID");
        db = new DB(getActivity());
        isDeleteShown = false;
    }
    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str =mSearchView.getText().toString();
            if(TextUtils.isEmpty(str)){
                recycleAdapter = new MyReyclerAdapter(getActivity(), mData);
                recyclerView.setAdapter(recycleAdapter);
            }
           else
                query(str);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.id_recycle);
        mSearchView  = (EditText) view.findViewById(R.id.search_view);
        mSearchView.addTextChangedListener(textWatcher);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView){
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int pos = viewHolder.getAdapterPosition();
                itemlayout = (ViewGroup)recyclerView.getChildAt(viewHolder.getAdapterPosition());
                if(longPos!=pos){
                    itemlayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    Data data = mData.get(pos);
                    startActivity(new Intent(getActivity(), DetailActivity.class)
                            .putExtra(DetailActivity.KEY_NOTE, data.getNote())
                            .putExtra(DetailActivity.KEY_ID,data.getId())
                            .putExtra(DetailActivity.KEY_POS,pos));
                }

            }

            @Override
            public void onItemLOngClick(final RecyclerView.ViewHolder viewHolder) {
                /*new AlertDialog.Builder(getActivity())
                        .setTitle("确定要删除该条笔记么?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();*/
                if(!isDeleteShown){
                    deleteButton = LayoutInflater.from(getContext()).inflate(R.layout.activity_delete,null);
                    Log.d("info","come-in");
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemlayout.removeView(deleteButton);
                            deleteButton = null;
                            isDeleteShown = false;
                            int pos = viewHolder.getAdapterPosition();
                            Data data = mData.get(pos);
                            mData.remove(pos);
                            recycleAdapter.notifyItemRemoved(pos);
                            recycleAdapter.notifyItemRangeChanged(0, mData.size() - pos);
                            db.delete(data.getId());
                            recycleAdapter.notifyDataSetChanged();
                        }
                    });
                    longPos = viewHolder.getAdapterPosition();
                    itemlayout = (ViewGroup)recyclerView.getChildAt(viewHolder.getAdapterPosition());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    itemlayout.addView(deleteButton, params);
                    isDeleteShown = true;
                }
                else{
                    itemlayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                }
            }
        });
        // mTextView = (TextView)view.findViewById(R.id.txt_content);
        //mTextView.setText(mContent);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        recycleAdapter = new MyReyclerAdapter(this.getActivity(),mData);
        layoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.addItemDecoration(new MyDecoration(this.getActivity().getApplicationContext(),MyDecoration.VERTICAL_LIST));

    }

    private void initData() {
        mData = new ArrayList<>();
        Cursor cr;
        cr = db.getAll(mAccount);
        if(cr!=null&&cr.moveToFirst()) {
            do {
                mData.add(new Data(cr.getInt(cr.getColumnIndex("noteid")),cr.getString(cr.getColumnIndex("note")),cr.getString(cr.getColumnIndex("time")),cr.getString(cr.getColumnIndex("user"))));
            } while (cr.moveToNext());
        }
    }

    class MyReyclerAdapter extends RecyclerView.Adapter<MyReyclerAdapter.MyViewHolder>{
        private List<Data> mData = new ArrayList<>();
        int index = 0;
        private int position;
        @Override
        public void onBindViewHolder(final MyReyclerAdapter.MyViewHolder holder, int position) {
            holder.tv.setText(mData.get(position).getNote());
            holder.first.setText(mData.get(position).getNote().substring(0,1));
            holder.time.setText(mData.get(position).getTime());

            //to capture the position before the context menu is loaded:
            /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setPosition(holder.getAdapterPosition());
                    return false;
                }
            });*/

        }

        public int getItemPosition(){
            return position;
        }

        public void setPosition(int position){
            this.position = position;
        }

        private Context mcontext;
        private LayoutInflater inflater;

        public MyReyclerAdapter(Context context,List<Data> datas){
            this.mcontext = context;
            this.mData = datas;
            inflater = LayoutInflater.from(mcontext);
        }
       /* public void addNote(String note){
            mData.add(0,note);
            notifyItemInserted(0);
        }*/
       public int getItemCount(){
            return mData.size();
       }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_record, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            TextView first;
            TextView time;
            public MyViewHolder(View view){
                super(view);
                tv = (TextView)view.findViewById(R.id.id_num);
                first = (TextView)view.findViewById(R.id.id_first);
                time = (TextView)view.findViewById(R.id.id_time);
            }
        }
    }
    public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener{


        private final RecyclerView recyclerView;
        private final GestureDetectorCompat mGestureDetector;

        public OnRecyclerItemClickListener(RecyclerView recyclerView){
            this.recyclerView=recyclerView;
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListener());
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);

        public abstract void onItemLOngClick(RecyclerView.ViewHolder viewHolder);

        private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

            public  boolean onSingleTapUp(MotionEvent event){
                View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                if (child != null){
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                    onItemClick(viewHolder);
                }
                return true;
            }

            public  void onLongPress(MotionEvent event){
                View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                if (child != null){
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                    onItemLOngClick(viewHolder);
                }
            }

        }
    }

    void query(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            mSearchList = new ArrayList<Data>();
            Cursor cr = db.query(newText);
            if (cr != null && cr.moveToFirst()) {
                do {
                    mSearchList.add(new Data(cr.getInt(cr.getColumnIndex("noteid")), cr.getString(cr.getColumnIndex("note")), cr.getString(cr.getColumnIndex("time")), cr.getString(cr.getColumnIndex("user"))));
                } while (cr.moveToNext());
            }
            recycleAdapter = new MyReyclerAdapter(getActivity(), mSearchList);
            recyclerView.setAdapter(recycleAdapter);
        }
    }

}


