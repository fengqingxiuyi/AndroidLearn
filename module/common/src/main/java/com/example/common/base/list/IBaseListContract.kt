package com.example.common.base.list

/**
 * 列表页 契约类
 */
interface IBaseListContract<T> {

  /**
   * 刷新数据成功处理
   * @param data T
   */
  fun refresh(data: T)

  /**
   * 显示错误缺省页
   */
  fun showErrEmptyView()

  /**
   * 加载更多数据成功处理
   * @param data T
   */
  fun loadMore(data: T)

  /**
   * 响应数据失败处理
   * @param code Int
   */
  fun failure(code: Int)

}