package com.pushtorefresh.bamboostorage.test.integration;

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;

import com.pushtorefresh.bamboostorage.ABambooStorageListener;
import com.pushtorefresh.bamboostorage.BambooStorage;
import com.pushtorefresh.bamboostorage.IBambooStorableItem;
import com.pushtorefresh.bamboostorage.test.app.TestStorableItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Artem Zinnatullin [artem.zinnatullin@gmail.com]
 */
public class ListenersNotifyingIntegrationTests extends AndroidTestCase {

    private static final long ASYNC_TEST_TIMEOUT = 1000;

    private static class TestBambooStorageListener extends ABambooStorageListener {

        private AtomicBoolean mAddCalled    = new AtomicBoolean(false);
        private AtomicBoolean mUpdateCalled = new AtomicBoolean(false);
        private AtomicBoolean mAddAllCalled = new AtomicBoolean(false);
        private AtomicBoolean mAddOrUpdateAllCalled   = new AtomicBoolean(false);
        private AtomicBoolean mRemoveItemCalled       = new AtomicBoolean(false);
        private AtomicBoolean mRemoveWithWhereCalled  = new AtomicBoolean(false);
        private AtomicBoolean mRemoveAllOfTypeCalled  = new AtomicBoolean(false);
        private AtomicBoolean mAnyCRUDOperationCalled = new AtomicBoolean(false);

        @Override public void onAdd(@NonNull IBambooStorableItem storableItem) {
            mAddCalled.set(true);
        }

        @Override public void onUpdate(@NonNull IBambooStorableItem storableItem, int count) {
            mUpdateCalled.set(true);
        }

        @Override
        public void onAddAll(@NonNull Collection<? extends IBambooStorableItem> storableItems) {
            mAddAllCalled.set(true);
        }

        @Override
        public void onAddOrUpdateAll(@NonNull Collection<? extends IBambooStorableItem> storableItems) {
            mAddOrUpdateAllCalled.set(true);
        }

        @Override public void onRemove(@NonNull IBambooStorableItem storableItem, int count) {
            mRemoveItemCalled.set(true);
        }

        @Override
        public void onRemove(@NonNull Class<? extends IBambooStorableItem> classOfStorableItems, String where, String[] whereArgs, int count) {
            mRemoveWithWhereCalled.set(true);
        }

        @Override
        public void onRemoveAllOfType(@NonNull Class<? extends IBambooStorableItem> classOfStorableItems, int count) {
            mRemoveAllOfTypeCalled.set(true);
        }

        @Override
        public void onAnyCRUDOperation(@NonNull Class<? extends IBambooStorableItem> classOfStorableItems) {
            mAnyCRUDOperationCalled.set(true);
        }

        // of course, any CRUD operations listener should be called

        public void assertOnlyAddCalled() {
            assertTrue(mAddCalled.get());
            assertFalse(mAddAllCalled.get());
            assertFalse(mUpdateCalled.get());
            assertFalse(mAddOrUpdateAllCalled.get());
            assertFalse(mRemoveItemCalled.get());
            assertFalse(mRemoveWithWhereCalled.get());
            assertFalse(mRemoveAllOfTypeCalled.get());
            assertTrue(mAnyCRUDOperationCalled.get());
        }

        public void assertOnlyAddAllCalled() {
            assertFalse(mAddCalled.get());
            assertTrue(mAddAllCalled.get());
            assertFalse(mUpdateCalled.get());
            assertFalse(mAddOrUpdateAllCalled.get());
            assertFalse(mRemoveItemCalled.get());
            assertFalse(mRemoveWithWhereCalled.get());
            assertFalse(mRemoveAllOfTypeCalled.get());
            assertTrue(mAnyCRUDOperationCalled.get());
        }

        public void assertOnlyUpdateCalled() {
            assertFalse(mAddCalled.get());
            assertFalse(mAddAllCalled.get());
            assertTrue(mUpdateCalled.get());
            assertFalse(mAddOrUpdateAllCalled.get());
            assertFalse(mRemoveItemCalled.get());
            assertFalse(mRemoveWithWhereCalled.get());
            assertFalse(mRemoveAllOfTypeCalled.get());
            assertTrue(mAnyCRUDOperationCalled.get());
        }

        public void assertOnlyAddOrUpdateAllCalled() {
            assertFalse(mAddCalled.get());
            assertFalse(mAddAllCalled.get());
            assertFalse(mUpdateCalled.get());
            assertTrue(mAddOrUpdateAllCalled.get());
            assertFalse(mRemoveItemCalled.get());
            assertFalse(mRemoveWithWhereCalled.get());
            assertFalse(mRemoveAllOfTypeCalled.get());
            assertTrue(mAnyCRUDOperationCalled.get());
        }

        public void assertOnlyRemoveCalled() {
            assertFalse(mAddCalled.get());
            assertFalse(mAddAllCalled.get());
            assertFalse(mUpdateCalled.get());
            assertFalse(mAddOrUpdateAllCalled.get());
            assertTrue(mRemoveItemCalled.get());
            assertFalse(mRemoveWithWhereCalled.get());
            assertFalse(mRemoveAllOfTypeCalled.get());
            assertTrue(mAnyCRUDOperationCalled.get());
        }

        public void assertOnlyRemoveWithWhereCalled() {
            assertFalse(mAddCalled.get());
            assertFalse(mAddAllCalled.get());
            assertFalse(mUpdateCalled.get());
            assertFalse(mAddOrUpdateAllCalled.get());
            assertFalse(mRemoveItemCalled.get());
            assertTrue(mRemoveWithWhereCalled.get());
            assertFalse(mRemoveAllOfTypeCalled.get());
            assertTrue(mAnyCRUDOperationCalled.get());
        }

        public void assertOnlyRemoveAllOfTypeCalled() {
            assertFalse(mAddCalled.get());
            assertFalse(mAddAllCalled.get());
            assertFalse(mUpdateCalled.get());
            assertFalse(mAddOrUpdateAllCalled.get());
            assertFalse(mRemoveItemCalled.get());
            assertFalse(mRemoveWithWhereCalled.get());
            assertTrue(mRemoveAllOfTypeCalled.get());
            assertTrue(mAnyCRUDOperationCalled.get());
        }
    }

    private BambooStorage mBambooStorage;

    @Override protected void setUp() throws Exception {
        super.setUp();

        if (mBambooStorage == null) {
            mBambooStorage = new BambooStorage(getContext(), "com.pushtorefresh.bamboostorage.test");
        }

        removeAllTestStorableItems();
    }

    private void removeAllTestStorableItems() {
        mBambooStorage.removeAllOfType(TestStorableItem.class);
        assertEquals(0, mBambooStorage.countOfItems(TestStorableItem.class));
    }

    private static void shouldBeTrueInNearFuture(AtomicBoolean atomicBoolean) {
        final long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < ASYNC_TEST_TIMEOUT) {
            try {
                Thread.sleep(5); // giving another threads time to do their work
            } catch (InterruptedException e) {
                fail("Thread was interrupted " + e);
            }

            if (atomicBoolean.get()) {
                return;
            }
        }

        fail("Should be true in future failed by timeout (" + ASYNC_TEST_TIMEOUT + " ms)");
    }

    public void testNotifyAboutAdd() {
        final TestStorableItem testStorableItem = TestStorableItemFactory.newRandomItem();

        TestBambooStorageListener addListener = new TestBambooStorageListener() {
            @Override public void onAdd(@NonNull IBambooStorableItem storableItem) {
                assertEquals(testStorableItem, storableItem);
                super.onAdd(storableItem);
            }
        };

        mBambooStorage.addListener(addListener);


        mBambooStorage.add(testStorableItem);

        shouldBeTrueInNearFuture(addListener.mAddCalled);
        addListener.assertOnlyAddCalled();

        mBambooStorage.removeListener(addListener);
    }

    public void testNotifyAboutUpdate() {
        final TestStorableItem testStorableItem = TestStorableItemFactory.newRandomItem();
        mBambooStorage.add(testStorableItem);

        TestBambooStorageListener updateListener = new TestBambooStorageListener() {
            @Override public void onUpdate(@NonNull IBambooStorableItem storableItem, int count) {
                assertEquals(testStorableItem, storableItem);
                assertEquals(1, count);
                super.onUpdate(storableItem, count);
            }
        };
        mBambooStorage.addListener(updateListener);

        mBambooStorage.update(testStorableItem);

        shouldBeTrueInNearFuture(updateListener.mUpdateCalled);
        updateListener.assertOnlyUpdateCalled();

        mBambooStorage.removeListener(updateListener);
    }

    public void testNotifyAboutAddAll() {
        final Collection<TestStorableItem> testStorableItems = TestStorableItemFactory.newRandomItems(3);

        TestBambooStorageListener addAllListener = new TestBambooStorageListener() {
            @Override
            public void onAddAll(@NonNull Collection<? extends IBambooStorableItem> storableItems) {
                assertEquals(testStorableItems, storableItems);
                super.onAddAll(storableItems);
            }
        };

        mBambooStorage.addListener(addAllListener);

        mBambooStorage.addAll(testStorableItems);

        shouldBeTrueInNearFuture(addAllListener.mAddAllCalled);
        addAllListener.assertOnlyAddAllCalled();

        mBambooStorage.removeListener(addAllListener);
    }

    public void testNotifyAboutAddOrUpdateAll() {
        final Collection<TestStorableItem> testStorableItems = TestStorableItemFactory.newRandomItems(3);

        TestBambooStorageListener addOrUpdateAllListener = new TestBambooStorageListener() {
            @Override
            public void onAddOrUpdateAll(@NonNull Collection<? extends IBambooStorableItem> storableItems) {
                assertEquals(testStorableItems, storableItems);
                super.onAddOrUpdateAll(storableItems);
            }
        };

        mBambooStorage.addListener(addOrUpdateAllListener);

        mBambooStorage.addOrUpdateAll(testStorableItems);

        shouldBeTrueInNearFuture(addOrUpdateAllListener.mAddOrUpdateAllCalled);
        addOrUpdateAllListener.assertOnlyAddOrUpdateAllCalled();

        mBambooStorage.removeListener(addOrUpdateAllListener);
    }

    public void testNotifyAboutAddOrUpdateAdd() {
        final TestStorableItem testStorableItem = TestStorableItemFactory.newRandomItem();

        TestBambooStorageListener addListener = new TestBambooStorageListener() {
            @Override public void onAdd(@NonNull IBambooStorableItem storableItem) {
                assertEquals(testStorableItem, storableItem);
                super.onAdd(storableItem);
            }
        };
        mBambooStorage.addListener(addListener);

        // no item to update, so add listener should be called
        mBambooStorage.addOrUpdate(testStorableItem);

        shouldBeTrueInNearFuture(addListener.mAddCalled);
        addListener.assertOnlyAddCalled();

        mBambooStorage.removeListener(addListener);
    }

    public void testNotifyAboutAddOrUpdateUpdate() {
        final TestStorableItem testStorableItem = TestStorableItemFactory.newRandomItem();

        // no item to update, so add listener should be called
        mBambooStorage.add(testStorableItem);

        TestBambooStorageListener updateListener = new TestBambooStorageListener() {
            @Override public void onUpdate(@NonNull IBambooStorableItem storableItem, int count) {
                assertEquals(testStorableItem, storableItem);
                assertEquals(1, count);
                super.onUpdate(storableItem, count);
            }
        };
        mBambooStorage.addListener(updateListener);

        mBambooStorage.addOrUpdate(testStorableItem);

        shouldBeTrueInNearFuture(updateListener.mUpdateCalled);
        updateListener.assertOnlyUpdateCalled();

        mBambooStorage.removeListener(updateListener);
    }

    public void testNotifyAboutRemove() {
        final TestStorableItem testStorableItem = TestStorableItemFactory.newRandomItem();
        mBambooStorage.add(testStorableItem);

        TestBambooStorageListener removeListener = new TestBambooStorageListener() {
            @Override public void onRemove(@NonNull IBambooStorableItem storableItem, int count) {
                assertEquals(testStorableItem, storableItem);
                assertEquals(1, count);
                super.onRemove(storableItem, count);
            }
        };
        mBambooStorage.addListener(removeListener);

        mBambooStorage.remove(testStorableItem);
        shouldBeTrueInNearFuture(removeListener.mRemoveItemCalled);
        removeListener.assertOnlyRemoveCalled();

        mBambooStorage.removeListener(removeListener);
    }

    public void testNotifyAboutRemoveWithWhereCalled() {
        final String testWhere = TestStorableItem.TableInfo.TEST_INT_FIELD + " = ?";
        final String[] testWhereArgs = { "1" };


        TestBambooStorageListener removeWithWhereListener = new TestBambooStorageListener() {
            @Override
            public void onRemove(@NonNull Class<? extends IBambooStorableItem> classOfStorableItems, String where, String[] whereArgs, int count) {
                assertEquals(TestStorableItem.class, classOfStorableItems);
                assertEquals(testWhere, where);
                assertTrue(Arrays.equals(testWhereArgs, whereArgs));
                super.onRemove(classOfStorableItems, where, whereArgs, count);
            }
        };

        mBambooStorage.addListener(removeWithWhereListener);
        mBambooStorage.remove(TestStorableItem.class, testWhere, testWhereArgs);

        shouldBeTrueInNearFuture(removeWithWhereListener.mRemoveWithWhereCalled);
        removeWithWhereListener.assertOnlyRemoveWithWhereCalled();
        mBambooStorage.removeListener(removeWithWhereListener);
    }

    public void testNotifyAboutRemoveAllOfTypeCalled() {
        TestStorableItem testStorableItem1 = TestStorableItemFactory.newRandomItem();
        TestStorableItem testStorableItem2 = TestStorableItemFactory.newRandomItem();
        TestStorableItem testStorableItem3 = TestStorableItemFactory.newRandomItem();

        mBambooStorage.add(testStorableItem1);
        mBambooStorage.add(testStorableItem2);
        mBambooStorage.add(testStorableItem3);

        TestBambooStorageListener removeAllOfTypeListener = new TestBambooStorageListener() {
            @Override
            public void onRemoveAllOfType(@NonNull Class<? extends IBambooStorableItem> classOfStorableItems, int count) {
                assertEquals(TestStorableItem.class, classOfStorableItems);
                assertEquals(3, count);
                super.onRemoveAllOfType(classOfStorableItems, count);
            }
        };
        mBambooStorage.addListener(removeAllOfTypeListener);

        mBambooStorage.removeAllOfType(TestStorableItem.class);
        shouldBeTrueInNearFuture(removeAllOfTypeListener.mRemoveAllOfTypeCalled);
        removeAllOfTypeListener.assertOnlyRemoveAllOfTypeCalled();

        mBambooStorage.removeListener(removeAllOfTypeListener);
    }
}
