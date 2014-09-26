package net.twisterrob.glide.gallery.wrappers;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

public class ModelLoaderFactoryWrapper<T, Y> implements ModelLoaderFactory<T, Y> {
    public static interface ExceptionHandler {
        public void handle(Throwable t);
    }

    private final ModelLoaderFactory<T, Y> wrapped;
    private final ExceptionHandler handler;

    public ModelLoaderFactoryWrapper(ModelLoaderFactory<T, Y> wrapped, ExceptionHandler handler) {
        this.wrapped = wrapped;
        this.handler = handler;
    }

    @Override
    public ModelLoader<T, Y> build(Context context, GenericLoaderFactory factories) {
        try {
            return new ModelLoaderWrapper<>(wrapped.build(context, factories), handler);
        } catch (RuntimeException | Error e) {
            handler.handle(e);
            throw e;
        }
    }

    @Override
    public void teardown() {
        try {
            wrapped.teardown();
        } catch (RuntimeException | Error e) {
            handler.handle(e);
            throw e;
        }
    }
}
