package com.wtuadn.demo.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wtuadn.demo.R;
import com.wtuadn.demo.bean.GoodsBean;
import com.wtuadn.demo.database.DBHelper;
import com.wtuadn.yrecyclerview.RecyclerCursorAdapter;
import com.wtuadn.yrecyclerview.RecyclerItemListener;
import com.wtuadn.yrecyclerview.YRecyclerView;

/**
 * Created by wtuadn on 2017/8/1.
 */

public class CursorAdapterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private YRecyclerView recyclerView;
    private MyAdapter myAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_cursor_adapter);
        initToolbar();
        initRV();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.getMenu().add(0, 0, 0, "添加header").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 1, 0, "删除header").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 2, 0, "添加footer").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 3, 0, "删除footer").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        TextView headerView = new TextView(CursorAdapterActivity.this);
                        headerView.setText("header " + myAdapter.getHeaderSize());
                        headerView.setGravity(Gravity.CENTER);
                        headerView.setMinimumHeight(200);
                        myAdapter.addHeaderView(headerView);
                        break;
                    case 1:
                        myAdapter.removeHeaderView(myAdapter.getHeaderSize() - 1);
                        break;
                    case 2:
                        TextView footerView = new TextView(CursorAdapterActivity.this);
                        footerView.setText("footer " + myAdapter.getFooterSize());
                        footerView.setGravity(Gravity.CENTER);
                        footerView.setMinimumHeight(200);
                        myAdapter.addFooterView(footerView);
                        break;
                    case 3:
                        myAdapter.removeFooterView(0);
                }
                return true;
            }
        });
    }

    private void initRV() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        RecyclerItemListener recyclerItemListener = new RecyclerItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                GoodsBean bean = myAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), bean.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View v, int position) {
                onItemClick(v, position);
                return true;
            }

            @Override
            public void onItemCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo, int position) {
            }
        };
        recyclerItemListener.clickable = true;
        recyclerItemListener.longClickable = true;//长按监听与上下文菜单冲突，不能同时设置
        recyclerItemListener.createContextMenuable = false;
        recyclerView.setRecyclerItemListener(recyclerItemListener);
    }

    private class MyAdapter extends RecyclerCursorAdapter<GoodsBean, MyAdapter.Holder> {

        public MyAdapter() {
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select id, name from goods order by id asc", null);
            changeCursor(cursor);
        }

        @NonNull
        @Override
        protected Holder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(new Button(parent.getContext()));
        }

        @Override
        public GoodsBean getItem(int position) {
            boolean bool = mCursor.moveToPosition(position - headerSize);
            if (!bool) return null;
            GoodsBean bean = new GoodsBean();
            bean.setId(mCursor.getInt(0));
            bean.setName(mCursor.getString(1));
            return bean;
        }

        @Override
        public void onBindNormalViewHolder(@NonNull Holder viewHolder, int position) {
            final GoodsBean bean = getItem(position);
            viewHolder.button.setText(bean.getName());
            //甚至可以直接
            //holdler.button.setText(mCursor.getString(1));
            //省掉在滑动时GoodsBean无限的创建，更加流畅，轻松支持1W级别的数据库
        }

        class Holder extends RecyclerView.ViewHolder {
            private Button button;

            Holder(Button itemView) {
                super(itemView);
                button = itemView;
            }
        }
    }
}
