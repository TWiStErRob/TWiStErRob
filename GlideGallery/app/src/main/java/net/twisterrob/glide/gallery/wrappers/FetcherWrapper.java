package net.twisterrob.glide.gallery.wrappers;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

class FetcherWrapper<T> implements DataFetcher<T> {
    private final DataFetcher<T> wrapped;
    private final ModelLoaderFactoryWrapper.ExceptionHandler handler;

    public FetcherWrapper(DataFetcher<T> wrapped, ModelLoaderFactoryWrapper.ExceptionHandler handler) {
        this.wrapped = wrapped;
        this.handler = handler;
    }

    @Override
    public T loadData(Priority priority) throws Exception {
        try {
            return wrapped.loadData(priority);
        } catch (RuntimeException | Error e) {
            handler.handle(e);
            throw e;
        }
    }

    @Override
    public void cleanup() {
        try {
            wrapped.cleanup();
        } catch (RuntimeException | Error e) {
            handler.handle(e);
            throw e;
        }
    }

    @Override
    public String getId() {
        try {
            return wrapped.getId();
        } catch (RuntimeException | Error e) {
            handler.handle(e);
            throw e;
        }
    }

    @Override
    public void cancel() {
        try {
            wrapped.cancel();
        } catch (RuntimeException | Error e) {
            handler.handle(e);
            throw e;
        }
    }
}
