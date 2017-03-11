package com.example.android.popularmovies.task;

/**
 * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain a hook back into the calling activity.
 *
 * @param <T> Task result
 * @param <Z> Task properties
 */
public interface AsyncTaskCompleteListener<T,Z> {
    /**
     * Invoked when the AsyncTask on pre execution.
     */
    public void onPreTaskExecute();
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     * @param properties The properties from the AsyncTask.
     */
    public void onTaskComplete(T result, Z properties);

}
