package com.pushtorefresh.bamboostorage;

import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * @author Artem Zinnatullin [artem.zinnatullin@gmail.com]
 */
public interface IBambooStorageNotifier {

    /**
     * Adds listener
     *
     * @param listener BambooStorage listener
     */
    void addListener(@NonNull ABambooStorageListener listener);

    /**
     * Removes listener
     *
     * @param listener BambooStorage listener
     */
    void removeListener(@NonNull ABambooStorageListener listener);

    /**
     * Should notify listeners about add operation
     *
     * @param storableItem added item
     */
    void notifyAboutAdd(@NonNull IBambooStorableItem storableItem);

    /**
     * Should notify listeners about update operation
     *
     * @param storableItem updated item
     * @param count        count of updated entries
     */
    void notifyAboutUpdate(@NonNull IBambooStorableItem storableItem, int count);

    /**
     * Should notify listeners about add of items from collection
     *
     * @param storableItems added collection of items
     */
    void notifyAboutAddAll(@NonNull Collection<? extends IBambooStorableItem> storableItems);

    /**
     * Should notify listeners about add or update items from collection
     *
     * @param storableItems collection of items
     */
    void notifyAboutAddOrUpdateAll(@NonNull Collection<? extends IBambooStorableItem> storableItems);

    /**
     * Should notify listeners about remove operation
     *
     * @param storableItem removed item
     * @param count        count of removed entries
     */
    void notifyAboutRemove(@NonNull IBambooStorableItem storableItem, int count);

    /**
     * Should notify listeners about remove operation
     *
     * @param classOfStorableItems class of removed storable items
     * @param where                where clause
     * @param whereArgs            args for binding to where clause, same format as for ContentResolver
     * @param count                count of removed entries
     */
    void notifyAboutRemove(@NonNull Class<? extends IBambooStorableItem> classOfStorableItems, String where, String[] whereArgs, int count);

    /**
     * Should notify listeners about remove all of type operation
     *
     * @param classOfStorableItems class of removed storable items
     * @param count                count of removed entries
     */
    void notifyAboutRemoveAllOfType(@NonNull Class<? extends IBambooStorableItem> classOfStorableItems, int count);
}
