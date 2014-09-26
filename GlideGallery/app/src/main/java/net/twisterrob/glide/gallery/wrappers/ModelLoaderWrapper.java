package net.twisterrob.glide.gallery.wrappers;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;

class ModelLoaderWrapper<T, Y> implements ModelLoader<T, Y> {
    private final ModelLoader<T, Y> wrapped;
    private final net.twisterrob.glide.gallery.wrappers.ModelLoaderFactoryWrapper.ExceptionHandler handler;

    public ModelLoaderWrapper(ModelLoader<T, Y> wrapped, net.twisterrob.glide.gallery.wrappers.ModelLoaderFactoryWrapper.ExceptionHandler handler) {
        this.wrapped = wrapped;
        this.handler = handler;
    }


    public DataFetcher<Y> getResourceFetcher(T model, int width, int height) {
        try {
            return new net.twisterrob.glide.gallery.wrappers.FetcherWrapper<>(wrapped.getResourceFetcher(model, width, height), handler);
        } catch (RuntimeException | Error e) {
            handler.handle(e);
            throw e;
        }
    }
}
