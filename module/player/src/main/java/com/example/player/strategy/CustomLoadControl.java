package com.example.player.strategy;


import android.content.Context;

import com.example.log.LogUtil;
import com.example.utils.network.NetworkUtil;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.util.Util;

/**
 * The default {@link LoadControl} implementation.
 * 新增功能：修改最大缓冲时间、移动网络下根据需要判断是否需要缓冲
 */
public class CustomLoadControl implements LoadControl {

    private static final String TAG = "CustomLoadControl";

    /**
     * The default minimum duration of media that the player will attempt to ensure is buffered at all
     * times, in milliseconds.
     */
    public static final int DEFAULT_MIN_BUFFER_MS = 15000;

    /**
     * The default maximum duration of media that the player will attempt to buffer, in milliseconds.
     */
    public static final int DEFAULT_MAX_BUFFER_MS = 50000;

    /**
     * The default duration of media that must be buffered for playback to start or resume following a
     * user action such as a seek, in milliseconds.
     */
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 2500;

    /**
     * The default duration of media that must be buffered for playback to resume after a rebuffer, in
     * milliseconds. A rebuffer is defined to be caused by buffer depletion rather than a user action.
     */
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 5000;

    /**
     * The default target buffer size in bytes. When set to {@link C#LENGTH_UNSET}, the load control
     * automatically determines its target buffer size.
     */
    public static final int DEFAULT_TARGET_BUFFER_BYTES = C.LENGTH_UNSET;

    /**
     * The default prioritization of buffer time constraints over size constraints.
     */
    public static final boolean DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS = true;

    /**
     * Builder for {@link CustomLoadControl}.
     */
    public static final class Builder {

        private DefaultAllocator allocator;
        private int minBufferMs;
        private int maxBufferMs;
        private int bufferForPlaybackMs;
        private int bufferForPlaybackAfterRebufferMs;
        private int targetBufferBytes;
        private boolean prioritizeTimeOverSizeThresholds;
        private PriorityTaskManager priorityTaskManager;

        /**
         * Constructs a new instance.
         */
        public Builder() {
            allocator = null;
            minBufferMs = DEFAULT_MIN_BUFFER_MS;
            maxBufferMs = DEFAULT_MAX_BUFFER_MS;
            bufferForPlaybackMs = DEFAULT_BUFFER_FOR_PLAYBACK_MS;
            bufferForPlaybackAfterRebufferMs = DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;
            targetBufferBytes = DEFAULT_TARGET_BUFFER_BYTES;
            prioritizeTimeOverSizeThresholds = DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS;
            priorityTaskManager = null;
        }

        /**
         * Sets the {@link DefaultAllocator} used by the loader.
         *
         * @param allocator The {@link DefaultAllocator}.
         * @return This builder, for convenience.
         */
        public Builder setAllocator(DefaultAllocator allocator) {
            this.allocator = allocator;
            return this;
        }

        /**
         * Sets the buffer duration parameters.
         *
         * @param minBufferMs                      The minimum duration of media that the player will attempt to ensure is
         *                                         buffered at all times, in milliseconds.
         * @param maxBufferMs                      The maximum duration of media that the player will attempt to buffer, in
         *                                         milliseconds.
         * @param bufferForPlaybackMs              The duration of media that must be buffered for playback to start
         *                                         or resume following a user action such as a seek, in milliseconds.
         * @param bufferForPlaybackAfterRebufferMs The default duration of media that must be buffered
         *                                         for playback to resume after a rebuffer, in milliseconds. A rebuffer is defined to be
         *                                         caused by buffer depletion rather than a user action.
         * @return This builder, for convenience.
         */
        public Builder setBufferDurationsMs(
                int minBufferMs,
                int maxBufferMs,
                int bufferForPlaybackMs,
                int bufferForPlaybackAfterRebufferMs) {
            this.minBufferMs = minBufferMs;
            this.maxBufferMs = maxBufferMs;
            this.bufferForPlaybackMs = bufferForPlaybackMs;
            this.bufferForPlaybackAfterRebufferMs = bufferForPlaybackAfterRebufferMs;
            return this;
        }

        /**
         * Sets the target buffer size in bytes. If set to {@link C#LENGTH_UNSET}, the target buffer
         * size will be calculated using {@link #calculateTargetBufferSize(Renderer[],
         * TrackSelectionArray)}.
         *
         * @param targetBufferBytes The target buffer size in bytes.
         * @return This builder, for convenience.
         */
        public Builder setTargetBufferBytes(int targetBufferBytes) {
            this.targetBufferBytes = targetBufferBytes;
            return this;
        }

        /**
         * Sets whether the load control prioritizes buffer time constraints over buffer size
         * constraints.
         *
         * @param prioritizeTimeOverSizeThresholds Whether the load control prioritizes buffer time
         *                                         constraints over buffer size constraints.
         * @return This builder, for convenience.
         */
        public Builder setPrioritizeTimeOverSizeThresholds(boolean prioritizeTimeOverSizeThresholds) {
            this.prioritizeTimeOverSizeThresholds = prioritizeTimeOverSizeThresholds;
            return this;
        }

        /**
         * Sets the {@link PriorityTaskManager} to use.
         */
        public Builder setPriorityTaskManager(PriorityTaskManager priorityTaskManager) {
            this.priorityTaskManager = priorityTaskManager;
            return this;
        }

        /**
         * Creates a {@link CustomLoadControl}.
         */
        public CustomLoadControl createCustomLoadControl() {
            if (allocator == null) {
                allocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);
            }
            return new CustomLoadControl(
                    allocator,
                    minBufferMs,
                    maxBufferMs,
                    bufferForPlaybackMs,
                    bufferForPlaybackAfterRebufferMs,
                    targetBufferBytes,
                    prioritizeTimeOverSizeThresholds,
                    priorityTaskManager);
        }
    }

    private final DefaultAllocator allocator;

    private final long minBufferUs;
    private long maxBufferUs;
    private final long bufferForPlaybackUs;
    private final long bufferForPlaybackAfterRebufferUs;
    private final int targetBufferBytesOverwrite;
    private final boolean prioritizeTimeOverSizeThresholds;
    private final PriorityTaskManager priorityTaskManager;

    private int targetBufferSize;
    private boolean isBuffering;

    //true 允许移动网络下缓冲 false 反之
    private boolean enableBufferOnMobile = false;
    //当前是否是移动网络连接状态
    private boolean isMobileConnected;

    public void setEnableBufferOnMobile(boolean enableBufferOnMobile) {
        this.enableBufferOnMobile = enableBufferOnMobile;
    }

    public void updateMaxBufferUs(long maxBufferUs) {
        this.maxBufferUs = maxBufferUs;
    }

    /**
     * Constructs a new instance, using the {@code DEFAULT_*} constants defined in this class.
     */
    public CustomLoadControl() {
        this(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));
    }

    public CustomLoadControl(Context context) {
        this(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));
        isMobileConnected = NetworkUtil.isMobileConnected(context);
    }

    /**
     * @deprecated Use {@link Builder} instead.
     */
    @Deprecated
    public CustomLoadControl(DefaultAllocator allocator) {
        this(
                allocator,
                DEFAULT_MIN_BUFFER_MS,
                DEFAULT_MAX_BUFFER_MS,
                DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
                DEFAULT_TARGET_BUFFER_BYTES,
                DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS);
    }

    /**
     * @deprecated Use {@link Builder} instead.
     */
    @Deprecated
    public CustomLoadControl(
            DefaultAllocator allocator,
            int minBufferMs,
            int maxBufferMs,
            int bufferForPlaybackMs,
            int bufferForPlaybackAfterRebufferMs,
            int targetBufferBytes,
            boolean prioritizeTimeOverSizeThresholds) {
        this(
                allocator,
                minBufferMs,
                maxBufferMs,
                bufferForPlaybackMs,
                bufferForPlaybackAfterRebufferMs,
                targetBufferBytes,
                prioritizeTimeOverSizeThresholds,
                null);
    }

    /**
     * @deprecated Use {@link Builder} instead.
     */
    @Deprecated
    public CustomLoadControl(
            DefaultAllocator allocator,
            int minBufferMs,
            int maxBufferMs,
            int bufferForPlaybackMs,
            int bufferForPlaybackAfterRebufferMs,
            int targetBufferBytes,
            boolean prioritizeTimeOverSizeThresholds,
            PriorityTaskManager priorityTaskManager) {
        assertGreaterOrEqual(bufferForPlaybackMs, 0, "bufferForPlaybackMs", "0");
        assertGreaterOrEqual(
                bufferForPlaybackAfterRebufferMs, 0, "bufferForPlaybackAfterRebufferMs", "0");
        assertGreaterOrEqual(minBufferMs, bufferForPlaybackMs, "minBufferMs", "bufferForPlaybackMs");
        assertGreaterOrEqual(
                minBufferMs,
                bufferForPlaybackAfterRebufferMs,
                "minBufferMs",
                "bufferForPlaybackAfterRebufferMs");
        assertGreaterOrEqual(maxBufferMs, minBufferMs, "maxBufferMs", "minBufferMs");

        this.allocator = allocator;
        minBufferUs = minBufferMs * 1000L;
        maxBufferUs = maxBufferMs * 1000L;
        bufferForPlaybackUs = bufferForPlaybackMs * 1000L;
        bufferForPlaybackAfterRebufferUs = bufferForPlaybackAfterRebufferMs * 1000L;
        targetBufferBytesOverwrite = targetBufferBytes;
        this.prioritizeTimeOverSizeThresholds = prioritizeTimeOverSizeThresholds;
        this.priorityTaskManager = priorityTaskManager;
    }

    @Override
    public void onPrepared() {
        reset(false);
    }

    @Override
    public void onTracksSelected(Renderer[] renderers, TrackGroupArray trackGroups,
                                 TrackSelectionArray trackSelections) {
        targetBufferSize =
                targetBufferBytesOverwrite == C.LENGTH_UNSET
                        ? calculateTargetBufferSize(renderers, trackSelections)
                        : targetBufferBytesOverwrite;
        allocator.setTargetBufferSize(targetBufferSize);
    }

    @Override
    public void onStopped() {
        reset(true);
    }

    @Override
    public void onReleased() {
        reset(true);
    }

    @Override
    public Allocator getAllocator() {
        return allocator;
    }

    @Override
    public long getBackBufferDurationUs() {
        return 0;
    }

    @Override
    public boolean retainBackBufferFromKeyframe() {
        return false;
    }

    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs, float playbackSpeed) {
        boolean targetBufferSizeReached = allocator.getTotalBytesAllocated() >= targetBufferSize;
        boolean wasBuffering = isBuffering;
        long minBufferUs = this.minBufferUs;
        if (playbackSpeed > 1) {
            // The playback speed is faster than real time, so scale up the minimum required media
            // duration to keep enough media buffered for a playout duration of minBufferUs.
            long mediaDurationMinBufferUs =
                    Util.getMediaDurationForPlayoutDuration(minBufferUs, playbackSpeed);
            minBufferUs = Math.min(mediaDurationMinBufferUs, maxBufferUs);
        }
        if (bufferedDurationUs < minBufferUs) {
            isBuffering = prioritizeTimeOverSizeThresholds || !targetBufferSizeReached;
        } else if (bufferedDurationUs > maxBufferUs || targetBufferSizeReached) {
            isBuffering = false;
        } // Else don't change the buffering state

        LogUtil.d(TAG, " enableBufferOnMobile = ", enableBufferOnMobile, " isMobileConnected = ", isMobileConnected, " maxBufferUs = ", maxBufferUs);
        //自定义逻辑，移动网络下看需要是否需要停止缓冲
        if (isMobileConnected && !enableBufferOnMobile) {
            isBuffering = false;
        }

        if (priorityTaskManager != null && isBuffering != wasBuffering) {
            if (isBuffering) {
                priorityTaskManager.add(C.PRIORITY_PLAYBACK);
            } else {
                priorityTaskManager.remove(C.PRIORITY_PLAYBACK);
            }
        }
        return isBuffering;
    }

    @Override
    public boolean shouldStartPlayback(
            long bufferedDurationUs, float playbackSpeed, boolean rebuffering) {
        bufferedDurationUs = Util.getPlayoutDurationForMediaDuration(bufferedDurationUs, playbackSpeed);
        long minBufferDurationUs = rebuffering ? bufferForPlaybackAfterRebufferUs : bufferForPlaybackUs;
        return minBufferDurationUs <= 0
                || bufferedDurationUs >= minBufferDurationUs
                || (!prioritizeTimeOverSizeThresholds
                && allocator.getTotalBytesAllocated() >= targetBufferSize);
    }

    /**
     * Calculate target buffer size in bytes based on the selected tracks. The player will try not to
     * exceed this target buffer. Only used when {@code targetBufferBytes} is {@link C#LENGTH_UNSET}.
     *
     * @param renderers           The renderers for which the track were selected.
     * @param trackSelectionArray The selected tracks.
     * @return The target buffer size in bytes.
     */
    protected int calculateTargetBufferSize(
            Renderer[] renderers, TrackSelectionArray trackSelectionArray) {
        int targetBufferSize = 0;
        for (int i = 0; i < renderers.length; i++) {
            if (trackSelectionArray.get(i) != null) {
                targetBufferSize += Util.getDefaultBufferSize(renderers[i].getTrackType());
            }
        }
        return targetBufferSize;
    }

    private void reset(boolean resetAllocator) {
        targetBufferSize = 0;
        if (priorityTaskManager != null && isBuffering) {
            priorityTaskManager.remove(C.PRIORITY_PLAYBACK);
        }
        isBuffering = false;
        if (resetAllocator) {
            allocator.reset();
        }
    }

    private static void assertGreaterOrEqual(int value1, int value2, String name1, String name2) {
        Assertions.checkArgument(value1 >= value2, name1 + " cannot be less than " + name2);
    }
}

