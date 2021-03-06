package com.robin.commonUi.ext


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.robin.commonUi.R
import com.robin.commonUi.customview.actionbar.TitleBar
import com.robin.commonUi.loadCallBack.EmptyCallback
import com.robin.commonUi.loadCallBack.ErrorCallback
import com.robin.commonUi.loadCallBack.LoadingCallback
import com.robin.commonUi.recyclerview.DefineLoadMoreView
import com.robin.commonUi.util.ColorUtils
import com.robin.commonUi.viewpager.ScaleTransitionPagerTitleView
import com.robin.jetpackmvvm.base.appContext
import com.robin.jetpackmvvm.ext.util.toHtml
import com.robin.jetpackmvvm.navigation.NavHostFragment
import com.robin.jetpackmvvm.network.stateCallback.ListDataUiState
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * ?????????: hegaojian
 * ?????????: 2020/2/20
 * ?????????:????????????????????????????????????
 */

fun LoadService<*>.setErrorText(message: String) {
    if (message.isNotEmpty()) {
        this.setCallBack(ErrorCallback::class.java) { _, view ->
            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

/**
 * ??????????????????
 * @param message ?????????????????????????????????
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}

/**
 * ???????????????
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * ???????????????
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //??????????????????????????????
        callback.invoke()
    }
    loadsir.showSuccess()
    ColorUtils.setLoadingColor(ColorUtils.getColor(appContext), loadsir)
    return loadsir
}

//???????????????Recyclerview
fun RecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

//??????SwipeRecyclerView
fun SwipeRecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): SwipeRecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

fun SwipeRecyclerView.initFooter(loadmoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
    val footerView = DefineLoadMoreView(appContext)
    //?????????????????????
    footerView.setLoadViewColor(ColorUtils.getOneColorStateList(appContext))
    //????????????????????????
    footerView.setLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
        footerView.onLoading()
        loadmoreListener.onLoadMore()
    })
    this.run {
        //????????????????????????
        addFooterView(footerView)
        setLoadMoreView(footerView)
        //????????????????????????
        setLoadMoreListener(loadmoreListener)
    }
    return footerView
}

fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //??????recyclerview?????????????????????????????????????????????????????????????????????
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    floatbtn.backgroundTintList = ColorUtils.getOneColorStateList(appContext)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //????????????recyclerview ?????????????????????????????????????????????40????????????????????????????????????????????????????????????????????????
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//?????????????????????????????????(??????)
        } else {
            smoothScrollToPosition(0)//??????????????????????????????(?????????)
        }
    }
}

//????????? SwipeRefreshLayout
fun SmartRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //??????????????????
        setPrimaryColorsId(ColorUtils.getColor(appContext))
    }
}

/**
 * ??????????????????toolbar ???????????????
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(ColorUtils.getColor(appContext))
    title = titleStr
    return this
}

/*
* ????????????????????? toolbar
* */
fun Toolbar.initCenterClose(
    context: Context,
    titleStr: String = "",
    backImg: Int = R.drawable.back_icon,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
    //setBackgroundColor(SettingUtil.getColor(appContext))
    setTitleCenter(context, titleStr)
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}

/*
* ????????????????????? toolbar
* */
fun Toolbar.initCenter(
    context: Context,
    titleStr: String = ""
): Toolbar {
    //setBackgroundColor(SettingUtil.getColor(appContext))
    setTitleCenter(context, titleStr)
    return this
}

/**
 * ????????????????????????toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    backImg: Int = R.drawable.ic_back,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(ColorUtils.getColor(appContext))
    title = titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}

/**
 * ????????????????????????????????????????????????????????????????????? ????????????????????????????????????????????? Textview???FragmentLayout???????????????????????????
 * ???????????????BottomNavigationViewEx????????????????????????FragmentLayout???????????? is Fragmentlayout????????? is BottomNavigationViewEx??????
 * ??????????????????????????? is FragmentLayout???????????? ?????? is BottomNavigationViewEx???????????????
 */
fun setUiTheme(color: Int, vararg anyList: Any?) {
    anyList.forEach { view ->
        view?.let {
            when (it) {
                is LoadService<*> -> ColorUtils.setLoadingColor(color, it as LoadService<Any>)
                is FloatingActionButton -> it.backgroundTintList =
                    ColorUtils.getOneColorStateList(color)
                is SmartRefreshLayout -> it.setPrimaryColorsId(color)
                is DefineLoadMoreView -> it.setLoadViewColor(ColorUtils.getOneColorStateList(color))
                is Toolbar -> it.setBackgroundColor(color)
                is TextView -> it.setTextColor(color)
                is LinearLayout -> it.setBackgroundColor(color)
                is ConstraintLayout -> it.setBackgroundColor(color)
                is FrameLayout -> it.setBackgroundColor(color)
                else -> {
                }
            }
        }
    }
}

//??????????????????????????????
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    //??????0????????????????????? ????????????
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}

fun MagicIndicator.bindViewPager2(
    viewPager: ViewPager2,
    mStringList: ArrayList<String> = arrayListOf(),
    action: (index: Int) -> Unit = {}
) {
    val commonNavigator = CommonNavigator(appContext)
    commonNavigator.adapter = object : CommonNavigatorAdapter() {
        override fun getCount(): Int {
            return mStringList.size

        }

        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return ScaleTransitionPagerTitleView(appContext).apply {
                text = mStringList[index].toHtml()
                textSize = 17f
                normalColor = Color.GRAY
                selectedColor = context.resources.getColor(R.color.colorPrimary)
                setOnClickListener {
                    viewPager.currentItem = index
                    action.invoke(index)
                }
            }
        }

        override fun getIndicator(context: Context): IPagerIndicator {
            return LinePagerIndicator(context).apply {
                mode = LinePagerIndicator.MODE_EXACTLY
                //??????????????????
                lineHeight = UIUtil.dip2px(appContext, 3.0).toFloat()
                lineWidth = UIUtil.dip2px(appContext, 50.0).toFloat()
                //???????????????
                roundRadius = UIUtil.dip2px(appContext, 6.0).toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //???????????????
                setColors(context.resources.getColor(R.color.colorPrimary))
            }
        }
    }
    this.navigator = commonNavigator

    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindViewPager2.onPageSelected(position)
            action.invoke(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindViewPager2.onPageScrollStateChanged(state)
        }
    })
}


fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //???????????????
    this.isUserInputEnabled = isUserInputEnabled
    //???????????????
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}


/**
 * ???????????????
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

/**
 * ??????????????????
 */
fun <T> loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    loadService: LoadService<*>,
    recyclerView: SwipeRecyclerView,
    smartRefreshLayout: SmartRefreshLayout
) {
    smartRefreshLayout.finishRefresh()
    recyclerView.loadMoreFinish(data.isEmpty, data.hasMore)
    if (data.isSuccess) {
        //??????
        when {
            //???????????????????????? ?????????????????????
            data.isFirstEmpty -> {
                loadService.showEmpty()
            }
            //????????????
            data.isRefresh -> {
                baseQuickAdapter.setList(data.listData)
                loadService.showSuccess()
            }
            //???????????????
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
    } else {
        //??????
        if (data.isRefresh) {
            //??????????????????????????????????????????????????????????????????
            loadService.showError(data.errMessage)
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}


fun Toolbar.setTitleCenter(
    context: Context,
    titleStr: String,
    textSize: Float = 22f,
    textColor: Int = Color.WHITE
) {
    val titleText = TextView(context)
    titleText.setTextColor(textColor)
    titleText.text = titleStr
    titleText.textSize = textSize
    titleText.typeface = Typeface.defaultFromStyle(Typeface.BOLD);
    titleText.gravity = Gravity.CENTER
    val layoutParams =
        Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT)
    layoutParams.gravity = Gravity.CENTER
    titleText.layoutParams = layoutParams
    addView(titleText)
}

fun <T> loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    loadService: LoadService<*>,
    recyclerView: SwipeRecyclerView
) {
    recyclerView.loadMoreFinish(data.isEmpty, data.hasMore)
    if (data.isSuccess) {
        //??????
        when {
            //???????????????????????? ?????????????????????
            data.isFirstEmpty -> {
                loadService.showEmpty()
            }
            //????????????
            data.isRefresh -> {
                baseQuickAdapter.setList(data.listData)
                loadService.showSuccess()
            }
            //???????????????
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
    } else {
        //??????
        if (data.isRefresh) {
            //??????????????????????????????????????????????????????????????????
            loadService.showError(data.errMessage)
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}

fun <T> loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    recyclerView: SwipeRecyclerView
) {
    recyclerView.loadMoreFinish(data.isEmpty, data.hasMore)
    if (data.isSuccess) {
        //??????
        when {
            //???????????????????????? ?????????????????????
            data.isFirstEmpty -> {

            }
            //????????????
            data.isRefresh -> {
                baseQuickAdapter.setList(data.listData)

            }
            //???????????????
            else -> {
                baseQuickAdapter.addData(data.listData)
            }
        }
    } else {
        //??????
        if (data.isRefresh) {
            //??????????????????????????????????????????????????????????????????
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}


//????????????
fun showLogOutDialog(activity: AppCompatActivity, logOutAction: () -> Unit = {}) {
    activity.showMessage("??????????????????", "??????", "??????", logOutAction, "??????")
}
