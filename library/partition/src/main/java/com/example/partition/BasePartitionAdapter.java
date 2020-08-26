package com.example.partition;

import android.util.SparseIntArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器基类
 */
public abstract class BasePartitionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 分区集合，根据数据源list创建，大小和数据源一样；
     * 目的是为了减少内存占用，不创建相同数据的重复分区；
     * partitionPosition是partitionList的index。
     */
    private List<BasePartition> partitionList;
    /**
     * 缓存viewType和partitionPosition的关系，key为viewType，value为partitionPosition；
     * 目的是在onCreateViewHolder方法中能够获取到partitionPosition。
     */
    private SparseIntArray viewTypePositionArr;
    /**
     * 集合的大小为getItemCount的返回值；
     * totalItemList的大小由业务类决定；
     * position是totalItemList的index。
     */
    private List<Integer> totalItemList;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int partitionPosition = totalItemList.get(position);
                    verifyPartitionPosition(partitionPosition);
                    BasePartition partition = partitionList.get(partitionPosition);
                    return partition.getSpanSize(getPartitionInnerPosition(position, partition));
                }
            });
        }
    }

    /**
     * 分区是否可复用由repeat决定；
     * 默认规则（只是一个说法并没有在代码中限制）：相同分区可复用，同一分区内新增的Header或Footer不可复用。
     * 举个例子：
     * 0 1      2 3      4 5      6 7      参数position
     * 1 0      2 0      3 0      1 0      业务类定义的viewType
     * 1 1+100  2 3+100  3 5+100  1 7+100  最终生成的viewType
     */
    @Override
    public int getItemViewType(int position) {
        int partitionPosition = totalItemList.get(position);
        verifyPartitionPosition(partitionPosition);
        BasePartition partition = partitionList.get(partitionPosition);
        ItemViewTypeBean bean = partition.getItemViewTypeBean(getPartitionInnerPosition(position, partition));
        if (bean == null) {
            throw new RuntimeException("BasePartitionAdapter getItemViewType ItemViewTypeBean == null");
        }
        if (bean.itemViewType < 0) {
            throw new RuntimeException("BasePartitionAdapter getItemViewType ItemViewTypeBean.itemViewType < 0");
        }
        int key;
        if (bean.repeat) {
            key = bean.itemViewType;
        } else {
            key = bean.itemViewType + partitionPosition * 1000;
        }
        viewTypePositionArr.put(key, partitionPosition);
        return key;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        /**
         * 此处逻辑与getItemViewType方法相对应
         */
        if (viewType >= 1000) {
            int partitionPosition = viewTypePositionArr.get(viewType);
            return partitionList.get(partitionPosition).onCreateViewHolder(viewGroup, viewType - partitionPosition * 1000);
        } else {
            return partitionList.get(viewTypePositionArr.get(viewType)).onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int partitionPosition = totalItemList.get(position);
        verifyPartitionPosition(partitionPosition);
        BasePartition partition = partitionList.get(partitionPosition);
        partition.onBindViewHolder(viewHolder, getPartitionInnerPosition(position, partition));
    }

    /**
     * getItemCount的值由业务类决定
     */
    @Override
    public int getItemCount() {
        return totalItemList.size();
    }

    /**
     * 创建分区集合
     */
    public void setData(List<IPartitionBean> list) {
        //数据初始化
        if (partitionList == null) {
            partitionList = new ArrayList<>();
        } else {
            partitionList.clear();
        }
        if (viewTypePositionArr == null) {
            viewTypePositionArr = new SparseIntArray();
        } else {
            viewTypePositionArr.clear();
        }
        if (totalItemList == null) {
            totalItemList = new ArrayList<>();
        } else {
            totalItemList.clear();
        }
        if (list == null) {
            return;
        }
        //创建数据
        int size = list.size();
        for (int i = 0; i < size; i++) {
            IPartitionBean bean = list.get(i);
            if (bean == null) {
                continue;
            }
            BasePartition partition = createPartition(bean);
            if (partition == null) {
                continue;
            }
            //设置分区数据
            partition.setStartCount(totalItemList.size());
            partition.setPosition(i);
            partition.setItemList();//setItemList必须在setPosition之后执行
            partition.setData(bean);
            partition.setPartitionType(bean.getPartitionType());
            partitionList.add(partition);
            //计算totalItemList
            totalItemList.addAll(partition.getItemList());
        }
    }

    /**
     * 重置分区集合
     */
    public void resetData(List<IPartitionBean> list) {
        setData(list);
        notifyDataSetChanged();
    }

    /**
     * 获取指定位置的分区，可能为null
     *
     * @param partitionPosition 通过BasePartition子类回调获取
     * @return 返回指定位置的分区
     */
    public BasePartition getPartition(int partitionPosition) {
        verifyPartitionPosition(partitionPosition);
        return partitionList.get(partitionPosition);
    }

    /**
     * 更新指定位置的分区
     *
     * @param bean              新分区数据源
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    public void updatePartition(IPartitionBean bean, int partitionPosition) {
        if (bean == null) {
            throw new RuntimeException("BasePartitionAdapter updatePartition bean == null");
        }
        verifyPartitionPosition(partitionPosition);
        //添加新分区到partitionList中
        BasePartition newPartition = createPartition(bean);
        if (newPartition == null) {
            throw new RuntimeException("BasePartitionAdapter updatePartition newPartition == null");
        }
        newPartition.setData(bean);
        newPartition.setPartitionType(bean.getPartitionType());
        partitionList.set(partitionPosition, newPartition);
        updatePartitionParam();
    }

    /**
     * 移除指定位置的分区
     *
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    public void removePartition(int partitionPosition) {
        verifyPartitionPosition(partitionPosition);
        //删除partitionList中数据
        partitionList.remove(partitionPosition);
        updatePartitionParam();
    }

    /**
     * 在指定位置添加新分区
     *
     * @param bean              新分区数据源
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    public void addPartition(IPartitionBean bean, int partitionPosition) {
        if (bean == null) {
            throw new RuntimeException("BasePartitionAdapter addPartition bean == null");
        }
        verifyPartitionPosition(partitionPosition);
        //添加新分区到partitionList中
        BasePartition newPartition = createPartition(bean);
        if (newPartition == null) {
            throw new RuntimeException("BasePartitionAdapter addPartition newPartition == null");
        }
        newPartition.setData(bean);
        newPartition.setPartitionType(bean.getPartitionType());
        partitionList.add(partitionPosition, newPartition);
        updatePartitionParam();
    }

    /**
     * 获取指定范围内的分区list，可能为null
     *
     * @param startPartitionPosition 通过BasePartition子类回调获取
     * @param endPartitionPosition   通过BasePartition子类回调获取
     * @return 返回指定位置的分区
     */
    public List<BasePartition> getPartitionList(int startPartitionPosition, int endPartitionPosition) {
        verifyPartitionPosition(startPartitionPosition, endPartitionPosition);
        return partitionList.subList(startPartitionPosition, endPartitionPosition);
    }

    /**
     * 将指定返回内的分区更改为新分区list
     *
     * @param list                   新分区数据源
     * @param startPartitionPosition 通过BasePartition子类回调获取
     * @param endPartitionPosition   通过BasePartition子类回调获取
     */
    public void updatePartitionList(List<IPartitionBean> list, int startPartitionPosition, int endPartitionPosition) {
        if (list == null || list.size() == 0) {
            throw new RuntimeException("BasePartitionAdapter updatePartitionList list == null || list.size() == 0");
        }
        verifyPartitionPosition(startPartitionPosition, endPartitionPosition);
        //移除指定范围的分区
        for (int i = startPartitionPosition; i < endPartitionPosition; i++) {
            partitionList.remove(startPartitionPosition);
        }
        //添加分区列表到partitionList中
        for (int i = 0; i < list.size(); i++) {
            IPartitionBean bean = list.get(i);
            if (bean == null) {
                continue;
            }
            BasePartition newPartition = createPartition(bean);
            if (newPartition == null) {
                continue;
            }
            newPartition.setData(bean);
            newPartition.setPartitionType(bean.getPartitionType());
            partitionList.add(startPartitionPosition + i, newPartition);
        }
        updatePartitionParam();
    }

    /**
     * 移除指定范围的分区
     *
     * @param startPartitionPosition 通过BasePartition子类回调获取
     * @param endPartitionPosition   通过BasePartition子类回调获取
     */
    public void removePartitionList(int startPartitionPosition, int endPartitionPosition) {
        verifyPartitionPosition(startPartitionPosition, endPartitionPosition);
        //移除指定范围的分区
        for (int i = startPartitionPosition; i < endPartitionPosition; i++) {
            partitionList.remove(startPartitionPosition);
        }
        updatePartitionParam();
    }

    /**
     * 在指定位置添加分区list
     *
     * @param list              新分区数据源
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    public void addPartitionList(List<IPartitionBean> list, int partitionPosition) {
        if (list == null || list.size() == 0) {
            throw new RuntimeException("BasePartitionAdapter addPartitionList list == null || list.size() == 0");
        }
        verifyPartitionPosition(partitionPosition);
        //添加分区列表到partitionList中
        for (int i = 0; i < list.size(); i++) {
            IPartitionBean bean = list.get(i);
            if (bean == null) {
                continue;
            }
            BasePartition newPartition = createPartition(bean);
            if (newPartition == null) {
                continue;
            }
            newPartition.setData(bean);
            newPartition.setPartitionType(bean.getPartitionType());
            partitionList.add(partitionPosition + i, newPartition);
        }
        updatePartitionParam();
    }

    /**
     * 更新集合数据，保证位置的正确性
     */
    private void updatePartitionParam() {
        //clear
        totalItemList.clear();
        int size = partitionList.size();
        for (int i = 0; i < size; i++) {
            BasePartition partition = partitionList.get(i);
            //设置分区数据
            partition.setStartCount(totalItemList.size());
            partition.setPosition(i);
            partition.setItemList();//setItemList必须在setPosition之后执行
            //计算totalItemList
            totalItemList.addAll(partition.getItemList());
        }
        notifyDataSetChanged();
    }

    /**
     * 使BasePartition子类得到正确的position，作用在传参上
     */
    private int getPartitionInnerPosition(int position, BasePartition partition) {
        /**
         * 减去partition的startCount是为了使partition得到自己的position，
         * 因为参数position是totalItemList的index。
         */
        return position - partition.getStartCount();
    }

    /**
     * 验证partitionPosition是否合法
     */
    private void verifyPartitionPosition(int partitionPosition) {
        if (partitionPosition < 0 || partitionPosition >= partitionList.size()) {
            throw new RuntimeException("BasePartitionAdapter verifyPartitionPosition partitionPosition is illegal");
        }
    }

    /**
     * 验证partitionPosition是否合法
     */
    private void verifyPartitionPosition(int startPartitionPosition, int endPartitionPosition) {
        if (startPartitionPosition < 0 || startPartitionPosition >= partitionList.size()) {
            throw new RuntimeException("BasePartitionAdapter verifyPartitionPosition startPartitionPosition is illegal");
        }
        if (endPartitionPosition < 0 || endPartitionPosition >= partitionList.size()) {
            throw new RuntimeException("BasePartitionAdapter verifyPartitionPosition endPartitionPosition is illegal");
        }
        if (startPartitionPosition > endPartitionPosition) {
            throw new RuntimeException("BasePartitionAdapter verifyPartitionPosition startPartitionPosition > endPartitionPosition");
        }
    }

    /**
     * 创建分区
     *
     * @return 可能为null
     */
    public abstract BasePartition createPartition(IPartitionBean bean);

}
