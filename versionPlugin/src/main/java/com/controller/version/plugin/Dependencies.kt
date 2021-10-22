package com.controller.version.plugin

object BuildVersions {
    const val compileSdkVersion = 29
    const val buildToolsVersion = "29.0.3"
    const val minSdkVersion = 21
    const val targetSdkVersion = 29
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Versions {
    const val android_plugin = "4.0.0"
    const val kotlin = "1.5.31"
    const val kotlin_coroutines = "1.5.2"
    const val coreKtx = "1.6.0"

    const val appcompat = "1.2.0"
    const val material = "1.4.0"
    const val jetpack_lifecycle = "2.3.0"
    const val jetpack_viewModel = "2.3.0"
    const val jetpack_room = "2.3.0"
    const val jetpack_navigation = "2.3.3"
    const val jetpack_paging = "3.0.0-alpha07"

    const val jetpack_activity = "1.2.0-beta01"
    const val jetpack_fragment = "1.2.5"
    const val viewPager2 = "1.0.0"

    const val gson = "2.8.6"
    const val retrofit = "2.9.0"
    const val dagger = "2.28-alpha"
    const val dagger_lifecycle = "1.0.0-alpha01"
    const val glide = "4.12.0"
    const val mmkv = "1.2.10"
    const val magicIndictor = "1.7.0"
    const val loadsir = "1.3.6"
    const val lottie = "3.4.0"
    const val materialDiagVersion = "3.3.0"
    const val bottomBar = "2.0.4"
    const val refresh = "1.1.2"

    const val rxjava = "2.2.4"
    const val rxkotlin = "2.2.0"
    const val rxandroid = "2.1.0"
    const val rxbinding = "3.1.0"
    const val autodispose = "1.2.0"

    const val leakcanary = "1.6.2"

    const val espresso = "3.2.0"
    const val robolectric = "3.3.2"
    const val koin ="2.2.2"
    const val dslTabLayout ="3.0.0"
}


object Dependencies {
    const val kotlinLibrary = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlinCoroutinesLibrary =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlin_coroutines}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    const val multiDex = "com.android.support:multidex:2.0.1"
    const val appcompatV4 = "androidx.legacy:legacy-support-v4:${Versions.appcompat}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val appcompatV13 = "androidx.legacy:legacy-support-v13:${Versions.appcompat}"
    const val materialDesign = "com.google.android.material:material:1.2.0-alpha03"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.appcompat}"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val annotations = "androidx.annotation:annotation:${Versions.appcompat}"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-alpha03"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val flexbox = "com.google.android:flexbox:1.1.0"
    const val material = "com.google.android.material:material:${Versions.material}"

    const val activity = "androidx.activity:activity:${Versions.jetpack_activity}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.jetpack_activity}"
    const val fragment = "androidx.fragment:fragment:${Versions.jetpack_fragment}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.jetpack_fragment}"
    const val fragmentTest = "androidx.fragment:fragment-testing:${Versions.jetpack_fragment}"
    const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"

//    const val lifecycleExtension = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val lifecycleJava8 =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.jetpack_lifecycle}"
    const val lifecycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${Versions.jetpack_lifecycle}"
    const val lifecycleKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.jetpack_lifecycle}"
    const val lifecycleProcess ="androidx.lifecycle:lifecycle-process:${Versions.jetpack_lifecycle}"

    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:${Versions.jetpack_viewModel}"
    const val viewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.jetpack_viewModel}"

    const val livedata = "androidx.lifecycle:lifecycle-livedata:${Versions.jetpack_lifecycle}"
    const val livedataRx =
        "androidx.lifecycle:lifecycle-reactivestreams:${Versions.jetpack_lifecycle}"
    const val livedataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.jetpack_lifecycle}"


    const val navigation = "androidx.navigation:navigation-fragment:${Versions.jetpack_navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Versions.jetpack_navigation}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.jetpack_navigation}"
    const val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.jetpack_navigation}"

    const val paging = "androidx.paging:paging-runtime:${Versions.jetpack_paging}"
    const val pagingRx = "androidx.paging:paging-rxjava2:${Versions.jetpack_paging}"

    const val room = "androidx.room:room-runtime:${Versions.jetpack_room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.jetpack_room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.jetpack_room}"
    const val roomRx = "androidx.room:room-rxjava2:${Versions.jetpack_room}"
    const val roomTesting = "androidx.room:room-testing:${Versions.jetpack_room}"

    const val okhttp = "com.squareup.okhttp3:okhttp:4.3.0"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:4.3.0"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitRx = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val PersistentCookieJar = "com.github.franmontiel:PersistentCookieJar:v1.0.1"
    const val retrofitUrlManager = "me.jessyan:retrofit-url-manager:1.4.0"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    const val rxandroid = "com.squareup.retrofit2:rxandroid:${Versions.rxandroid}"
    const val rxkotlin = "com.squareup.retrofit2:rxkotlin:${Versions.rxkotlin}"

    const val rxbinding = "com.jakewharton.rxbinding3:rxbinding-core:${Versions.rxbinding}"
    const val rxbindingAppcompat =
        "com.jakewharton.rxbinding3:rxbinding-appcompat:${Versions.rxbinding}"
    const val rxbindingRecyclerview =
        "com.jakewharton.rxbinding3:rxbinding-recyclerview:${Versions.rxbinding}"
    const val rxbindingSwipeRefresh =
        "com.jakewharton.rxbinding3:rxbinding-swiperefreshlayout:${Versions.rxbinding}"

    const val autoDispose = "com.uber.autodispose:autodispose-android:${Versions.autodispose}"
    const val autoDisposeKtx = "com.uber.autodispose:autodispose-ktx:${Versions.autodispose}"
    const val autoDisposeAndroidKtx =
        "com.uber.autodispose:autodispose-android-ktx:${Versions.autodispose}"
    const val autoDisposeArchsKtx =
        "com.uber.autodispose:autodispose-android-archcomponents-ktx:${Versions.autodispose}"

    const val daggerHilt = "com.google.dagger:hilt-android:${Versions.dagger}"
    const val daggerAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.dagger}"

    const val daggerViewModel =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.dagger_lifecycle}"
    const val daggerHiltCompiler = "androidx.hilt:hilt-compiler:${Versions.dagger_lifecycle}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    const val baseAdapter = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4"
    const val utils = "com.blankj:utilcodex:1.29.0"
    const val download = "com.download.library:Downloader:4.1.3"
    const val fileDownloader = "com.liulishuo.filedownloader:library:1.7.7"
    const val mmkv = "com.tencent:mmkv-static:${Versions.mmkv}"
    const val magicIndicator = "com.github.hackware1993:MagicIndicator:${Versions.magicIndictor}"
    const val swipeRecycleView = "com.yanzhenjie.recyclerview:x:1.3.2"
    const val loadSir = "com.kingja.loadsir:loadsir:${Versions.loadsir}"
    const val calendarView = "com.haibin:calendarview:3.6.9"

    //material Dialog
    const val dialogLifecycle =
        "com.afollestad.material-dialogs:lifecycle:${Versions.materialDiagVersion}"
    const val dialogCore = "com.afollestad.material-dialogs:core:${Versions.materialDiagVersion}"
    const val dialogColor = "com.afollestad.material-dialogs:color:${Versions.materialDiagVersion}"
    const val dialogDateTime = "com.afollestad.material-dialogs:datetime:${Versions.materialDiagVersion}"
    const val dialogBottomSheets =
        "com.afollestad.material-dialogs:bottomsheets:${Versions.materialDiagVersion}"

    const val bottomBar = "com.github.ittianyu:BottomNavigationViewEx:${Versions.bottomBar}"
    const val bottomNavigation ="it.sephiroth.android.library.bottomnavigation:bottom-navigation:3.0.0"
    const val SmartRefreshLayout = "com.scwang.smartrefresh:SmartRefreshLayout:${Versions.refresh}"
    const val permission = "com.yanzhenjie:permission:2.0.3"
    const val autoSize = "me.jessyan:autosize:1.2.1"
    const val banner = "com.youth.banner:banner:2.0.8"
    const val immersionbar = "com.gyf.immersionbar:immersionbar:3.0.0"
    const val zxingLite = "com.king.zxing:zxing-lite:1.1.9-androidx"
    const val pickerView = "com.contrarywind:Android-PickerView:4.1.9"
    const val androidanimations = "com.daimajia.androidanimations:library:2.4@aar"
    const val customOnCrash = "cat.ereza:customactivityoncrash:2.3.0"
    const val basePopup = "com.github.razerdp:BasePopup:2.2.10"

    // Koin AndroidX Scope feature
    const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    // Koin AndroidX ViewModel feature
    const val KoinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    // Koin AndroidX Fragment Factory (unstable version)
    const val koinFragmet = "org.koin:koin-androidx-fragment:${Versions.koin}"

    const val leakCanaryDebug = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"
    const val leakCanaryDebugSupport =
        "com.squareup.leakcanary:leakcanary-support-fragment:${Versions.leakcanary}"
    const val leakCanaryRelease =
        "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakcanary}"

    const val junit4 = "junit:junit:4.13"

    const val mockito_kotlin = "com.nhaarman:mockito-kotlin:1.5.0"

    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val robolectricV4 = "org.robolectric:shadows-support-v4:${Versions.robolectric}"

    const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:3.2.0"
    const val espressoIdlingResource = "androidx.test.espresso:espresso-idling-resource:3.2.0"
    const val testRunner = "androidx.test:runner:1.1.0"
    const val testRules = "androidx.test:rules:1.1.0"

    const val dslTabLayout ="com.github.angcyo.DslTablayout:TabLayout:${Versions.dslTabLayout}"
    const val dslViewPager2Delegate ="com.github.angcyo.DslTablayout:ViewPager2Delegate:${Versions.dslTabLayout}"
}
