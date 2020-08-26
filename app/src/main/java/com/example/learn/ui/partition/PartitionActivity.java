package com.example.learn.ui.partition;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.ui.partition.partition.one.OneBean;
import com.example.learn.ui.partition.partition.two.TwoBean;
import com.example.learn.ui.partition.utils.Util;
import com.example.partition.IPartitionBean;
import com.example.partition.ISpanSize;

import java.util.ArrayList;
import java.util.List;

public class PartitionActivity extends AppCompatActivity implements IPartitionCallback {

    //findView
    private RecyclerView mainRecyclerView;
    //adapter
    private PartitionAdapter partitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initRecyclerView();
        initAdapter();
    }

    private void initRecyclerView() {
        //findView
        mainRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        //set layout manager
        mainRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), ISpanSize.ONE));
    }

    private void initAdapter() {
        //create adapter
        partitionAdapter = new PartitionAdapter(getApplicationContext(), this);
        partitionAdapter.setData(Util.getData());
        //set adapter
        mainRecyclerView.setAdapter(partitionAdapter);
    }

    public void testUpdate(View view) {
//        mainAdapter.resetData(null);

//        Toast.makeText(this, "partition = " + mainAdapter.getPartition(1), Toast.LENGTH_SHORT).show();

//        OneBean oneBean = new OneBean();
//        List<OneBean.DataBean> dataBeanList = new ArrayList<>();
//        OneBean.DataBean dataBean = new OneBean.DataBean();
//        dataBean.setText("新增的分区1-数据");
//        dataBeanList.add(dataBean);
//        oneBean.setDataBean(dataBeanList);
//        oneBean.setLineHeight(50);
//        mainAdapter.updatePartition(oneBean, 2);

//        mainAdapter.removePartition(1);

//        OneBean oneBean = new OneBean();
//        List<OneBean.DataBean> dataBeanList = new ArrayList<>();
//        OneBean.DataBean dataBean = new OneBean.DataBean();
//        dataBean.setText("新增的分区1-数据");
//        dataBeanList.add(dataBean);
//        oneBean.setDataBean(dataBeanList);
//        oneBean.setLineHeight(50);
//        mainAdapter.addPartition(oneBean, 1);

//        Toast.makeText(this, "partition = " + mainAdapter.getPartitionList(1, 2), Toast.LENGTH_SHORT).show();

//        List<IPartitionBean> list = new ArrayList<>();
//        OneBean oneBean = new OneBean();
//        List<OneBean.DataBean> dataBeanList = new ArrayList<>();
//        OneBean.DataBean dataBean = new OneBean.DataBean();
//        dataBean.setText("更新的分区1-数据");
//        dataBeanList.add(dataBean);
//        oneBean.setDataBean(dataBeanList);
//        oneBean.setLineHeight(50);
//        list.add(oneBean);
//        TwoBean twoBean = new TwoBean();
//        List<TwoBean.DataBean> dataBeanList1 = new ArrayList<>();
//        TwoBean.DataBean dataBean1 = new TwoBean.DataBean();
//        dataBean1.setText("新增的分区2数据-1");
//        dataBeanList1.add(dataBean1);
//        TwoBean.DataBean dataBean2 = new TwoBean.DataBean();
//        dataBean2.setText("新增的分区2数据-2");
//        dataBeanList1.add(dataBean2);
//        twoBean.setDataBean(dataBeanList1);
//        twoBean.setLineHeight(1);
//        list.add(twoBean);
//        mainAdapter.updatePartitionList(list, 1, 3);

//        mainAdapter.removePartitionList(1, 3);

        List<IPartitionBean> list = new ArrayList<>();
        OneBean oneBean = new OneBean();
        List<OneBean.DataBean> dataBeanList = new ArrayList<>();
        OneBean.DataBean dataBean = new OneBean.DataBean();
        dataBean.setText("新增的分区1-数据");
        dataBeanList.add(dataBean);
        oneBean.setDataBean(dataBeanList);
        oneBean.setLineHeight(50);
        list.add(oneBean);
        TwoBean twoBean = new TwoBean();
        List<TwoBean.DataBean> dataBeanList1 = new ArrayList<>();
        TwoBean.DataBean dataBean1 = new TwoBean.DataBean();
        dataBean1.setText("新增的分区2数据-1");
        dataBeanList1.add(dataBean1);
        TwoBean.DataBean dataBean2 = new TwoBean.DataBean();
        dataBean2.setText("新增的分区2数据-2");
        dataBeanList1.add(dataBean2);
        twoBean.setDataBean(dataBeanList1);
        twoBean.setLineHeight(1);
        list.add(twoBean);
        partitionAdapter.addPartitionList(list, 1);
    }

    @Override
    public void clickOne(int position, String text) {
        Toast.makeText(this, "position = " + position + " , text = " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clickTwo(int position, String text) {
        Toast.makeText(this, "position = " + position + " , text = " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clickThree(int position, String text) {
        Toast.makeText(this, "position = " + position + " , text = " + text, Toast.LENGTH_SHORT).show();
    }
}
