#Why RxJava
RxJava RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM

关键词  Asynchronous Sequences Event-based

应用开发归根到底就是对应用流程的管理：先做什么、后做什么、发生某些情况时需要做相应的操作，但随着应用功能的不断叠加，以及使用网络环境的影响，应用的处理流程往往会越来越复杂。例如Android开发中，我们就经常会遇到界面交互与后台网络操作相互混杂的情况，在一个界面中，随着功能的添加与迭代，这些交互与网络操作会不断叠加，很快会变成一个无法控制的混乱代码。
                 
而RxJava提供了一种对复杂流程的管理方法。

几个关键概念

* Observable   被观察者：执行操作流程
* Observer、Subscriber      观察者：操作流程结束后的调用（异常退出）
* create、just、from、zip、combine ：提供多种方式创建操作流程
* map、flatmap 提供执行过程中传递参数的转换操作（1:1，1:n）
* subscribe()  关联观察者与被观察者
* Schedulers  设置执行序列所在线程（IO线程、CPU密集计算、UI线程）

来看一个最简单的例子，通过RxJava实现一个简单的网络图片和文字（JSON数据）加载功能。

实现流程

1. Android UI线程不能有网络操作，需要创建一个子线程执行网络操作；
2. JSON解析，得到文字信息和图片URL；
3. 网络图片加载，在UI线程显示图片和文字信息；

具体代码

    Observable.create(new Observable.OnSubscribe<RequestBean>() {
            @Override
            public void call(final Subscriber<? super RequestBean> subscriber) {
                OkHttpUtils.get("url").execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        RequestBean requestBean = JSONTools.parseObject(s, RequestBean.class);
                        subscriber.onNext(requestBean);
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
        // 此处可添加中间处理流程及对象转化操作
        .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
            }
        }).subscribe(new Subscriber<RequestBean>() {

            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(RequestBean requestBean) {
                Log.d(TAG, "onNext: " + requestBean.getReason());
                mNewsAdapter.addAll(requestBean.getResult().getData());
            }
        });
     }

从代码量上看可能并不会比普通实现少多少（可能还会多一点），但从代码逻辑来看，相比AsyncTask和Handler的实现更加“优雅”，避免了代码的跳转以及设置全局状态量，代码处理逻辑高度内聚。

###Conclusion：
1. RxJava可以完成我们通过AsyncTask及Handler实现的大多数功能，而使用RxJava可以避免创建一些监听状态的全局变量，代码的跳转及“迷之缩进”,让代码更加“优雅”。
2. 通过RxJava的线程管理我们可以细粒度的控制操作流程中每个状态的线程跳转（比如在一个操作流程中，我们可以在IO线程读取数据，CPU密集计算线程做数据处理，然后在UI线程显示结果，而这些我们通过添加几行简单的代码就可以实现）
3. 通过doOnSubscribe()、OnNext()、OnComplete()和OnError()，我们可以轻松地监听被观察者的运行状态，进行相应的处理
4. RxJava提供了基于Observable序列实现的异步调用，我们可以在Observable的创建时可以添加多个事件，序列化执行，同时，在操作流程中，可以使用map、flatMap将操作对象做1:1、1:N的转化，转化之后的对仍是一个Observable序列，并添加在主序列中（如网络请求数据转化，获取对象的集合属性，使用第三方库是需要对运行结果进行转化后使用）。

###Tips：
* RxJava是一个可以处理任何事件的通用框架，你可以在Observable中执行任何操作（网络请求、操作事件、数据库查询、文件读写...），在Observer中执行器响应事件。
* RxJava仅仅是一个容器，在其中你可以根据需求使用各种第三方库