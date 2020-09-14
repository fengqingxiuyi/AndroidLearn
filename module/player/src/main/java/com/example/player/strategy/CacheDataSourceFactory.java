package com.example.player.strategy;

import android.content.Context;

import com.example.utils.LogUtil;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

/**
 * 创建一个自定义缓存数据源工厂，实现文件缓存
 */
public class CacheDataSourceFactory implements DataSource.Factory {

    private static final String TAG = "CacheDataSourceFactory";
    //静态常量
    private static final long DEFAULT_MAX_CACHE_SIZE = 10 * 1024 * 1024;
    private static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024;
    //应用上下文
    private final Context context;
    //生成通过其加载媒体数据的DataSource实例
    private final DataSource.Factory defaultDataSourceFactory;
    //内存缓存最大值，单位是字节
    private long maxCacheSize;
    //文件缓存最大值，单位是字节
    private long maxFileSize;

    public CacheDataSourceFactory(Context context, DefaultBandwidthMeter bandwidthMeter, long maxCacheSize, long maxFileSize) {
        this.context = context.getApplicationContext();
        this.maxCacheSize = maxCacheSize;
        this.maxFileSize = maxFileSize;
        if (bandwidthMeter == null) {
            bandwidthMeter = new DefaultBandwidthMeter();
        }
        defaultDataSourceFactory = buildDataSourceFactory(bandwidthMeter);
    }

    /**
     * 生成通过其加载媒体数据的DataSource实例
     */
    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(context, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    /**
     * Util.getUserAgent 返回一个包含包名version的字符串
     */
    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(context, context.getPackageName()), bandwidthMeter);
    }

    @Override
    public DataSource createDataSource() {
        if (maxCacheSize <= 0) {
            maxCacheSize = DEFAULT_MAX_CACHE_SIZE;
        }
        if (maxFileSize <= 0) {
            maxFileSize = DEFAULT_MAX_FILE_SIZE;
        }
        LogUtil.d(TAG, " maxCacheSize = ", maxCacheSize, " maxFileSize = ", maxFileSize);
        File dir = new File(context.getCacheDir(), "media");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (SimpleCache.isCacheFolderLocked(dir)) {
            SimpleCache.disableCacheFolderLocking();
        }
        SimpleCache simpleCache = new SimpleCache(dir, new LeastRecentlyUsedCacheEvictor(maxCacheSize));
        return new CacheDataSource(simpleCache, defaultDataSourceFactory.createDataSource(),
                new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
    }

}
