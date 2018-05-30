package com.wtuadn.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.wtuadn.yrecyclerview.HolderListener;
import com.wtuadn.yrecyclerview.LoadRecyclerView;
import com.wtuadn.yrecyclerview.RecyclerListAdapter;
import com.wtuadn.yrecyclerview.lor.LoadOrRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wtuadn on 2017/8/1.
 */

public class ListAdapterActivity extends AppCompatActivity implements LoadOrRefreshView.OnLORListener {
    private Toolbar toolbar;
    private LoadOrRefreshView lor;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_adapter);
        initToolbar();
        initLOR();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.getMenu().add(0, 0, 0, "添加header").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 1, 0, "删除header").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 2, 0, "添加footer").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 3, 0, "删除footer").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 4, 0, "清空列表").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.getMenu().add(0, 5, 0, "切换方向").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        TextView headerView = new TextView(ListAdapterActivity.this);
                        headerView.setText("header " + myAdapter.getHeaderSize());
                        headerView.setGravity(Gravity.CENTER);
                        headerView.setMinimumHeight(200);
                        myAdapter.addHeaderView(headerView);
                        break;
                    case 1:
                        myAdapter.removeHeaderView(myAdapter.getHeaderSize() - 1);
                        break;
                    case 2:
                        TextView footerView = new TextView(ListAdapterActivity.this);
                        footerView.setText("footer " + myAdapter.getFooterSize());
                        footerView.setGravity(Gravity.CENTER);
                        footerView.setMinimumHeight(200);
                        myAdapter.addFooterView(footerView);
                        break;
                    case 3:
                        myAdapter.removeFooterView(0);
                        break;
                    case 4:
                        myAdapter.lists.clear();
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 5:
                        LinearLayoutManager layoutManager = (LinearLayoutManager) lor.getLoadRecyclerView().getLayoutManager();
                        layoutManager.setOrientation(1 - layoutManager.getOrientation());
                }
                return true;
            }
        });
    }

    private void initLOR() {
        lor = findViewById(R.id.lor);
        lor.setOnLORListener(this);
        LoadRecyclerView loadRecyclerView = lor.getLoadRecyclerView();
        loadRecyclerView.setColorSchemeColors(Color.BLUE);
        myAdapter = new MyAdapter(new ArrayList<GoodsBean>());
        loadRecyclerView.setAdapter(myAdapter);

        class ListenerImpl extends HolderListener<MyAdapter.Holder> implements View.OnClickListener, View.OnCreateContextMenuListener {
            @Override
            public void onClick(View v) {
                int position = HolderListener.getLayoutPosition(v);
                GoodsBean bean = myAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), bean.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewLayoutPosition();
                MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                };
                GoodsBean bean = myAdapter.getItem(position);
                menu.add(0, 1, 0, bean.getName() + "   菜单1").setOnMenuItemClickListener(listener);
                menu.add(0, 2, 0, bean.getName() + "   菜单2").setOnMenuItemClickListener(listener);
                menu.add(0, 3, 0, bean.getName() + "   菜单3").setOnMenuItemClickListener(listener);
            }

            @Override
            public void onHolderAttach(MyAdapter.Holder holder) {
                holder.button.setOnClickListener(this);
                holder.button.setOnCreateContextMenuListener(this);
            }
        }
        loadRecyclerView.addOnChildAttachStateChangeListener(new ListenerImpl());
        lor.autoRefresh();//自动刷新

        TextView emptyView = new TextView(this);
        emptyView.setText("EmptyView");
        emptyView.setTextColor(Color.RED);
        emptyView.setTextSize(30);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadRecyclerView.registerEmptyView(emptyView, true);//设置数据为空时的提示view

    }

    @Override
    public void onRefresh(final LoadOrRefreshView lor) {
        lor.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<GoodsBean> dataList = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    GoodsBean bean = new GoodsBean();
                    bean.setId(i);
                    bean.setName("商品" + i);
                    dataList.add(bean);
                }
                myAdapter.lists.clear();
                myAdapter.lists.addAll(dataList);
                myAdapter.notifyDataSetChanged();
                lor.finishLOR();
                lor.getLoadRecyclerView().setCanLoad(true);
            }
        }, 1000);
    }

    @Override
    public void onLoad(final LoadOrRefreshView lor) {
        lor.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<GoodsBean> dataList = new ArrayList<>();
                for (int i = myAdapter.lists.size(); i < myAdapter.lists.size() + 15; i++) {
                    GoodsBean bean = new GoodsBean();
                    bean.setId(i);
                    bean.setName("商品" + i);
                    dataList.add(bean);
                }
                int start = myAdapter.getItemCount() - 1;
                myAdapter.lists.addAll(dataList);
                myAdapter.notifyItemRangeInserted(start, dataList.size());
                lor.finishLOR();
                if (myAdapter.lists.size() >= 45) {
                    //加载了全部数据关闭上拉加载
                    lor.getLoadRecyclerView().setCanLoad(false);
                }
            }
        }, 1000);
    }

    private class MyAdapter extends RecyclerListAdapter<GoodsBean, MyAdapter.Holder> {

        public MyAdapter(List<GoodsBean> lists) {
            super(lists);
        }

        @NonNull
        @Override
        protected Holder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(new Button(parent.getContext()));
        }

        @Override
        public void onBindNormalViewHolder(@NonNull Holder viewHolder, int position) {
            final GoodsBean bean = getItem(position);
            viewHolder.button.setText(bean.getName());
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
