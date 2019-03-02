package net.acomputerdog.advplugin.async;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AsyncTask<T> implements Runnable {
    private final Plugin plugin;
    private final AsyncBlock<T> task;
    private final SyncBlock<T> callback;

    public boolean fail = false;

    public Exception exception;
    public String errorMessage;

    public T result;

    public AsyncTask(Plugin plugin, AsyncBlock<T> task) {
        this(plugin, task, null);
    }

    public AsyncTask(Plugin plugin, AsyncBlock<T> task, SyncBlock<T> callback) {
        this.plugin = plugin;
        this.task = task;
        this.callback = callback;
    }

    public AsyncTask(Plugin plugin, AsyncBlock<T> async, SyncBlock<T> onError, SyncBlock<T> onSuccess) {
        this(plugin, async, new SplitResultBlock<>(onError, onSuccess));
    }

    @Override
    public void run() {
        try {
            task.run(this);
        } catch (Exception e) {
            this.fail = true;
            this.exception = e;
        }

        if (callback != null) {
            Bukkit.getScheduler().runTask(plugin, () -> callback.run(this));
        }
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public interface AsyncBlock<T> {
        void run(AsyncTask<T> task) throws Exception;
    }

    public interface SyncBlock<T> {
        void run(AsyncTask<T> task);
    }

    private static class SplitResultBlock<T> implements SyncBlock<T> {
        private final SyncBlock<T> onError;
        private final SyncBlock<T> onSuccess;

        private SplitResultBlock(SyncBlock<T> onError, SyncBlock<T> onSuccess) {
            this.onError = onError;
            this.onSuccess = onSuccess;
        }

        @Override
        public void run(AsyncTask<T> task) {
            if (task.isFail()) {
                onError.run(task);
            } else {
                onSuccess.run(task);
            }
        }
    }
}
