# android-page

## 前言
android 分页列表数据加载引擎，主要封装了android分页列表数据加载的各个组件，如果你有一个需要分页加载的List列表，都可以使用此框架实现。该框架主要的功能有：
- 自动维护了分页信息
- 支持分页数据请求器的扩展。
- 支持上拉加载下拉刷新组件的扩展。
- 支持加载视图、空视图的定制。
- 支持加载失败点击重试的功能。
- 支持当前列表数据的检索。
- 支持当前列表数据的选择（单选和多选）
- 支持列表数据的状态保存与恢复

[![](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164901.png)](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164901.png)
[![](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164912.png)](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164912.png)
[![](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164915.png)](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164915.png)
[![](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164922.png)](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164922.png)
[![](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164948.png)](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164948.png)
[![](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164951.png)](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164951.png)
[![](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164957.png)](https://raw.githubusercontent.com/694551594/android-page/master/screenshots/截屏_20161012_164957.png)

## 基本介绍
该框架主要有如下主要组件：
- 分页列表数据请求器：IPageRequester，主要负责客户端向服务端的数据请求以及数据回调，框架自带了Retrofit+okhttp的请求器，如过你想使用其他请求器，请参考自带示例里的：OkHttpPageRequester
- 分页列表数据解析器：IPageDataParser，主要负责解析从服务器请求到的数据，获取要适配到AbsListView或者RecyclerView这些分页列表上面的数据集以及列表数据的总个数。
- 分页列表数据拦截器：IPageDataIntercept，主要负责对解析后以及适配到列表之前的数据的拦截，有的时候我们可能要对这部分数据进行特殊处理，那我们就可以使用分页列表数据拦截器。
- 分页列表数据管理器：PageManager，主要负责对请求器、解析器、拦截器的调度管理，此外，PageManager里面还维护了一个Page对象，用来保存分页信息，比如分页大小，数据总大小以及当前页码等等，供数据请求器使用。
- 分页列表数据适配器：IPageAdapter，主要负责将解析后的数据适配到Adapter上并显示出来，如ListAdapter、ExpandableListAdapter以及RecyclerAdapter，只要实现了此接口，都是可以进行数据适配的。
- 上拉加载下拉刷新组件提供器：OnPullToRefreshProvider，主要负责提供上拉加载下拉刷新组件，我们这里提供了XListView、XExpandableListView以及google的SwipeRefreshLayout组件，如果你使用的是其他的上拉加载下拉刷新组件，你需要实现OnPullToRefreshProvider接口。
- 分页列表主引擎：PageEngine，主要对上述各组件进行调度管理，以及对外提供主要操作接口。
- 分页列表视图、加载视图、空视图提供器：IPageViewProvider，主要提供分页列表视图、正在加载视图以及空视图的布局，如果你想定制正在加载以及空视图的布局，你需要实现此接口。
- 分页列表监听器：OnPageListener，涵盖了整个请求过程的生命周期。最常用的可能就是onPageLoadComplete方法了，这个方法在数据适配到adapter后回调，也就是说，你可以在这个方法里面拿到最终适配并显示出来的数据。
- 分页上下文：PageContext，是对PageEngine的UI层级的封装了，主要封装一些接口给Activity以及Fragment提供了，此外，PageContext里面提供了一个分页的配置类PageConfig，用于一些分页的基本配置，比如分页大小、是否在初始的时候自动加载数据等等。
- 分页上下文提供器：IPageContextProvider，其实就是对PageContext需要的一些组件的对外接口了，Activity以及Fragment需要实现此接口里面的方法。
- 分页列表数据检索器：IPageSearcher，主要负责对当前列表数据进行检索。
- 分页列表数据选择器：IPageChecker，主要负责对当前列表数据的单选或者多选功能。
- 分页列表数据状态保存与恢复：OnPageDataStateSaved，默认是列表数据实现Serializable接口，如果你使用的是其它序列化方式，你需要实现自己的OnPageDataStateSaved。

主要流程：
客户端发起加载请求，调用IPageRequester请求数据并回调，IPageDataParser解析回调数据，获取要适配的数据以及数据总数，IPageDataIntercept拦截器对数据进行拦截，IPageAdapter将解析后的数据适配到PageView上。此过程第一次请求会根据pageSize以及dataTotal初始化一个page对象，然后上拉加载下拉刷新的时候增加页号，以后的每一次请求都会将此对象传递给数据请求器请求数据。

## gradle 配置
``compile 'cn.yhq:android-page:3.6.0'``

## 使用方式

### 1、加载本地数据

####（1）同步加载
当前的Activity需要继承``SimplePageDataActivity<T>``,实现的方法有四个，注意，如果开启了初始化的时候自动加载数据的功能，设置setContentView以及获取findViewById的代码需要写到onViewCreated方法里。
```java
 @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new SimplePageAdapter(this);
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public IPageAdapter<String> getPageAdapter() {
        return mPageAdapter;
    }

    // 如果是非耗时操作，则可以直接返回要适配的数据
    @Override
    public List<String> getPageData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("条目" + i);
        }
        return data;
    }
```

####（2）异步加载
异步加载的时候就需要实现IPageRequester方法了，在需要回调数据的地方调用callCacheResponse或者callNetworkResponse就可以了，注意，这2个方法需要在UI线程里面调用。
```java
    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new SimplePageAdapter(this);
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public IPageAdapter<String> getPageAdapter() {
        return mPageAdapter;
    }

    // 如果是非耗时操作，则不需要重写此方法
    // 如果是耗时操作，则需要重写
    @Override
    public IPageRequester<List<String>, String> getPageRequester() {
        return new PageRequester<List<String>, String>(this) {
            private Thread thread;

            @Override
            public void onCancel() {
                thread.interrupt();
            }

            @Override
            public void executeRequest(Context context, PageAction pageAction, Page<String> page) {
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 1:
                                callCacheResponse((List<String>) msg.obj);
                                break;
                            case 2:
                                callNetworkResponse((List<String>) msg.obj);
                                break;
                        }
                    }
                };
                // 模拟耗时操作，1秒后获取缓存数据并返回，2秒后获取请求数据返回
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg1 = new Message();
                        msg1.obj = getCacheData();
                        msg1.what = 1;
                        handler.sendMessage(msg1);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg2 = new Message();
                        msg2.obj = getNetworkData();
                        msg2.what = 2;
                        handler.sendMessage(msg2);
                    }
                });
                thread.start();
            }
        };
    }

    private List<String> getNetworkData() {
        List<String> data = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            data.add("请求条目" + i);
//        }
        return data;
    }

    private List<String> getCacheData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("缓存条目" + i);
        }
        return data;
    }

    @Override
    public List<String> getPageData() {
        return null;
    }
```

### 2、加载网络数据（异步）

这里使用了我封装的另一个框架作为数据请求器：-[android-http](https://github.com/694551594/android-http "android-http")
当前的activity需要继承``RetrofitPageDataActivity<T, I>``，并实现如下接口，这里需要注意的是IPageDataParser的实现。
```java
  @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_network_page);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new AlbumPageAdapter(this);
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public void onPageConfig(PageConfig pageConfig) {
        super.onPageConfig(pageConfig);
        pageConfig.setPageSize(5);
    }

    @Override
    public Call<AlbumInfo> executePageRequest(int pageSize, int currentPage, Tracks mData) {
        return HttpAPIClient.getAPI().getAlbumInfo("夜曲", pageSize, currentPage);
    }

    @Override
    public IPageAdapter<Tracks> getPageAdapter() {
        return mPageAdapter;
    }

    @Override
    public IPageDataParser<AlbumInfo, Tracks> getPageDataParser() {
        return new IPageDataParser<AlbumInfo, Tracks>() {

            @Override
            public List<Tracks> getPageList(AlbumInfo data, boolean isFromCache) {
                return data.getTracks();
            }

            @Override
            public long getPageTotal(AlbumInfo data, boolean isFromCache) {
                return data.getTotal_tracks();
            }
        };
    }
```

adapter的实现：
```java
public class SimplePageAdapter extends PageListAdapter<String> {

    public SimplePageAdapter(Context context) {
        super(context);
        this.register(new ItemViewProvider2<String>() {
            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void setupView(ViewHolder viewHolder, int position, String entity) {
                viewHolder.bindTextData(android.R.id.text1, entity);
            }

            @Override
            public boolean isForProvider(int position, String entity) {
                return true;
            }
        });
    }
}
```

Fragment只需要继承``RetrofitPageDataFragment<T, I>``以及``PageDataFragment<T, I>``即可。

### 3、自定义网络加载框架

下面使用的是OkhttpUtils的例子：

自定义数据请求器：
```java
public abstract class OkHttpPageRequester<T, I> extends PageRequester<T, I> {
    private RequestCall mRequestCall;

    public OkHttpPageRequester(Context context) {
        super(context);
    }

    public abstract RequestCall getRequestCall(int pageSize, int pageIndex, I data);

    @Override
    public void executeRequest(final Context context, PageAction pageAction, Page<I> page) {
        mRequestCall = getRequestCall(page.pageSize, page.currentPage, page.mData);
        mRequestCall
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, "数据请求失败，请稍后重试", Toast.LENGTH_LONG).show();
                        callException(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = ((ParameterizedType) OkHttpPageRequester.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        T entity = new Gson().fromJson(response, type);
                        callNetworkResponse(entity);
                    }
                });
    }

    @Override
    public void onCancel() {
        mRequestCall.cancel();
    }
}
```

当前activity继承``PageDataActivity<T, I>``：
```java
 @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_network_page);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new AlbumPageAdapter(this);
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public void onPageConfig(PageConfig pageConfig) {
        super.onPageConfig(pageConfig);
        pageConfig.setPageSize(5);
    }

    @Override
    public IPageAdapter<Tracks> getPageAdapter() {
        return mPageAdapter;
    }

    @Override
    public IPageRequester<AlbumInfo, Tracks> getPageRequester() {
        return new OkHttpPageRequester<AlbumInfo, Tracks>(this) {

            @Override
            public RequestCall getRequestCall(int pageSize, int pageIndex, Tracks data) {
                return OkHttpUtils.get()
                        .url("http://v5.pc.duomi.com/search-ajaxsearch-searchall")
                        .addParams("kw", "夜曲")
                        .addParams("pz", String.valueOf(pageSize))
                        .addParams("pi", String.valueOf(pageIndex))
                        .build();
            }
        };
    }

    @Override
    public IPageDataParser<AlbumInfo, Tracks> getPageDataParser() {
        return new IPageDataParser<AlbumInfo, Tracks>() {

            @Override
            public List<Tracks> getPageList(AlbumInfo data, boolean isFromCache) {
                return data.getTracks();
            }

            @Override
            public long getPageTotal(AlbumInfo data, boolean isFromCache) {
                return data.getTotal_tracks();
            }
        };
    }
```

当然，如果你用了Volley等其他的http加载框架，你只需要继承PageRequester实现相应的接口，然后返回即可。

### 4、添加下拉刷新上拉加载功能
（1）使用XListView，只需要在布局里面把ListView替换为XListView即可：
```xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"

    tools:context="cn.yhq.page.sample.MainActivity">

    <com.markmao.pulltorefresh.widget.XListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerVertical="true"
        android:layout_alignParentRight="true"
        android:layout_alignParentEnd="true" />
</RelativeLayout>

```

（2）使用SwipeRefreshLayout：

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"

    tools:context="cn.yhq.page.sample.MainActivity">

    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swiperefreshlayout"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:scrollbars="vertical">

        <com.markmao.pulltorefresh.widget.XListView
            android:id="@+id/list_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_alignParentEnd="true"
            android:layout_alignParentRight="true"
            android:layout_centerVertical="true" />

    </android.support.v4.widget.SwipeRefreshLayout>
</RelativeLayout>

```


然后在当前的Activity里面实现方法：

```java
    @Override
    public OnPullToRefreshProvider getOnPullToRefreshProvider() {
        return new PullToRefreshSwipeLayoutListViewContext(mSwipeRefreshLayout, mListView);
    }
```

### 5、自定义下拉刷新上拉加载框架

（1）实现OnPullToRefreshProvider接口。

（2）在当前的当前的Activity里面返回你自定义的OnPullToRefreshProvider实现类。
      
```java
   @Override
   public OnPullToRefreshProvider getOnPullToRefreshProvider() {
       return new CustomPullToRefreshContext();
   }
 ```

### 6、拦截器的用法

（1）拦截器是处于IO线程，不可以直接更新UI。

（2）在当前的当前的Activity里面实现public void addPageDataIntercepts(List<IPageDataIntercept<String>> intercepts)方法，调用intercepts.add(new IPageDataIntercept() {}) 添加拦截器。
      
```java
   @Override
      public void addPageDataIntercepts(List<IPageDataIntercept<String>> intercepts) {
          intercepts.add(new IPageDataIntercept<String>() {
              @Override
              public List<String> intercept(Chain<String> chain) throws Exception {
                  List<String> data = chain.data();
                  data.add(0, "拦截器增加的条目");
                  return chain.handler(data);
              }
          });
      }
 ```

### 7、自定义加载视图与空视图

在当前的activity重写getPageViewProvider()方法：

```java
 @Override
    public IPageViewProvider getPageViewProvider() {
        return new IPageViewProvider() {

            @Override
            public View getPageView() {
                return SimplePageActivity2.this.getPageView();
            }

            @Override
            public int getPageLoadingView() {
                return R.layout.custom_loading_view;
            }

            @Override
            public int getPageEmptyView() {
                return R.layout.custom_empty_view;
            }
        };
    }
```

### 8、列表数据检索

（1）最简单的方式是调用attachSearchEditText方法，直接附加检索的EditText控件，这样当EditText检索内容改变时，列表会自动检索关键字并刷新界面。注意：此接口还需要传递IFilterName接口的实现，用来获取你要参与检索的文本内容。

（2）如果你要自定义检索接口，请调用setPageSearcher方法，并实现IPageSearcher接口，具体可以参考系统默认的实现cn.yhq.page.core.DefaultPageSearcher。

```java
this.attachSearchEditText(mEditText, new IFilterName<Tracks>() {
            @Override
            public String getFilterName(Tracks entity) {
                return entity.getTitle();
            }
        });
```

### 9、列表数据选择

（1）如果你的列表需要实现单选或者多选功能，你需要继承RetrofitPageCheckedActivity类，并调用setPageChecker方法设置选择器的选择类型和默认选择以及不可选择的数据接口。然后通过调用getPageChecker()方法获取数据选择器调用相关的方法。

```java
this.setPageChecker(PageChecker.CHECK_MODEL_MUTIPLE, new OnPageCheckedChangeListener<Tracks>() {
            @Override
            public void onPageCheckedChanged(List<Tracks> checkedList, int count) {
                mAllCheckButton.setChecked(isAllChecked());
                mOKButton.setText("选择(" + count + ")");
            }
        }, new OnPageCheckedInitListener<Tracks>() {
            @Override
            public boolean isEnable(int position, Tracks entity) {
                if (position == 0 || position == 8) {
                    return false;
                }
                return true;
            }
            @Override
            public boolean isChecked(int position, Tracks entity) {
                if (position == 1 || position == 9) {
                    return true;
                }
                return false;
            }
        });
```