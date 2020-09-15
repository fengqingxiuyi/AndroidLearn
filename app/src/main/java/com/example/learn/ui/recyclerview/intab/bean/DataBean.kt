package com.example.learn.ui.recyclerview.intab.bean

import androidx.fragment.app.Fragment

class DataBean {

    var head : HeadBean? = null

    inner class HeadBean {
        var text = ""
    }

    var pager : PagerBean? = null

    inner class PagerBean {
        var list : ArrayList<Fragment>? = null
    }

}