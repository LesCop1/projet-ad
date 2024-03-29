package fr.bcecb.resources;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import fr.bcecb.util.Log;
import fr.bcecb.util.Resources;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ResourceManager implements AutoCloseable {
    private final Map<Class<? extends IResource>, ResourceHandle> DEFAULT_RESOURCES = Maps.newHashMap();
    private final Map<ResourceHandle, IResource> LOADED_RESOURCES = Maps.newHashMap();

    public ResourceManager() {
        DEFAULT_RESOURCES.put(Shader.class, Resources.DEFAULT_SHADER);
        DEFAULT_RESOURCES.put(Texture.class, Resources.DEFAULT_TEXTURE);
        DEFAULT_RESOURCES.put(Font.class, Resources.DEFAULT_FONT);

        DEFAULT_RESOURCES.put(Profile.class, Resources.DEFAULT_PROFILE_FILE);
    }

    public <R extends IResource> R getResourceOrDefault(ResourceHandle<R> handle, ResourceHandle<R> defaultHandle) {
        return getResource(MoreObjects.firstNonNull(handle, defaultHandle));
    }

    public <R extends IResource> R getResource(ResourceHandle<R> handle) {
        if (handle == null) {
            return null;
        }

        R resource;
        if (!LOADED_RESOURCES.containsKey(handle)) {
            resource = loadResource(handle);
            LOADED_RESOURCES.put(handle, resource);
        } else resource = (R) LOADED_RESOURCES.get(handle);

        return resource;
    }

    public <R extends IResource> R loadResource(ResourceHandle<R> handle) {
        if (handle == null) {
            return null;
        }

        if (!resourceExists(handle)) {
            ResourceHandle<R> fallbackHandle = DEFAULT_RESOURCES.get(handle.getTypeToken().getRawType());
            Log.SYSTEM.warning("Resource {0} does not exist. Fallback to {1}", handle, fallbackHandle);
            handle = fallbackHandle;
        }

        try {
            InputStream inputStream = getInputStream(handle);

            if (inputStream == null) {
                throw new FileNotFoundException(handle.getHandle());
            }

            R resource = handle.getTypeToken().constructor(handle.getTypeToken().getRawType().getDeclaredConstructor()).invoke(null);
            resource.load(inputStream);

            if (!resource.validate()) {
                throw new InstantiationException("Failed to validate");
            }

            return resource;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException | InstantiationException e) {
            Log.SYSTEM.warning("Couldn't create resource for {0} : {1} ({2})", handle.getHandle(), e.getClass(), e.getLocalizedMessage());
            return null;
        }
    }

    private boolean resourceExists(ResourceHandle resource) {
        File file = new File(resource.getHandle());
        return file.exists() || ResourceManager.class.getClassLoader().getResource(resource.getHandle()) != null;
    }

    private InputStream getInputStream(ResourceHandle resource) throws FileNotFoundException {
        File file = new File(resource.getHandle());
        return file.exists() ? new FileInputStream(file) : ResourceManager.class.getClassLoader().getResourceAsStream(resource.getHandle());
    }

    @Override
    public void close() {
        for (IResource resource : LOADED_RESOURCES.values()) {
            resource.dispose();
        }
    }
}
