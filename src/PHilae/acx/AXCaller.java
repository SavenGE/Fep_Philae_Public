/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import PHilae.model.LGItem;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author Pecherk
 */
public final class AXCaller extends APLog implements Serializable {

    private String callerTag = "exec";
    private final HashMap<String, LGItem> calls = new HashMap<>();

    public AXCaller() {
    }

    public AXCaller(String tag) {
        setCallerTag(tag);
    }

    @Override
    public void logEvent(Object event) {
        logEvent(null, event);
    }

    @Override
    public void logDebug(Object event) {
        if (APController.isDebugMode()) {
            setCall("debug", event);
        }
    }

    @Override
    public void logDebug(String key, Object event) {
        if (APController.isDebugMode()) {
            setCall(key, event);
        }
    }

    @Override
    public void logDebug(String key, Object event, String prefix) {
        if (APController.isDebugMode()) {
            setCall(key, event, prefix);
        }
    }

    @Override
    public void logEvent(Object input, Object event) {
        if (!isBlank(input)) {
            setCall("input", event);
        }
        if (event instanceof Throwable) {
            logError((Throwable) event);
        } else {
            setCall("info", event);
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("<" + getCallerTag() + ">");
        for (LGItem logItem : sortArray(getCalls().values().toArray(new LGItem[getCalls().size()]), true)) {
            if (logItem.getPayload() instanceof Throwable) {
                String clazzName = ((Throwable) logItem.getPayload()).getClass().getSimpleName();
                if (clazzName.contains("XAPIException")) {
                    buffer.append("\r\n").append(getIndent()).append("<exception>").append(convertToString(logItem.getPayload())).append("</exception>");
                } else {
                    printError(buffer, (Throwable) logItem.getPayload(), null, null, "exception", getIndent());
                }
            } else {
                buffer.append("\r\n").append(getIndent()).append("<").append(logItem.getKey()).append(">").append(APController.isDebugMode() ? checkBlank(logItem.getPrefix(), "") : "").append(cleanText(convertToString(logItem.getPayload()))).append("</").append(logItem.getKey()).append(">");
            }
        }

        buffer.append("\r\n").append("</").append(getCallerTag()).append(">");
        return buffer.toString();
    }

    public <T> T[] sortArray(T[] array, boolean ascending) {
        Arrays.sort(array, ascending ? ((Comparator<T>) (T o1, T o2) -> ((Comparable) o1).compareTo(o2)) : ((Comparator<T>) (T o1, T o2) -> ((Comparable) o2).compareTo(o1)));
        return array;
    }

    /**
     * @return the calls
     */
    public HashMap<String, LGItem> getCalls() {
        return calls;
    }

    @Override
    public void logError(Throwable ex) {
        setCall("error", ex);
    }

    @Override
    public void setCall(String callRef, Object callObject) {
        setCall(callRef, callObject, false, 0, null);
    }

    @Override
    public void setCall(String callRef, Object callObject, long position) {
        setCall(callRef, callObject, false, position, null);
    }

    @Override
    public void setCall(String callRef, Object callObject, String prefix) {
        setCall(callRef, callObject, false, 0, prefix);
    }

    @Override
    public void setCall(String callRef, Object callObject, boolean replace) {
        setCall(callRef, callObject, replace, 0, null);
    }

    @Override
    public void setCall(String callRef, Object callObject, boolean replace, long position) {
        setCall(callRef, callObject, replace, position, null);
    }

    @Override
    public void setCall(String callRef, Object callObject, boolean replace, long position, String prefix) {
        if (!isBlank(callObject)) {
            getCalls().put(String.format("%02d", (position <= 0 ? (replace ? getCalls().size() - 1 : getCalls().size()) : position)) + (callRef = callRef.toLowerCase()), new LGItem(callRef, prefix, callObject, position, getCalls().size()));
        }
    }

    public void dump(PrintStream p, String indent) {
        p.print(indentLines(toString(), indent));
    }

    /**
     * @return the callerTag
     */
    public String getCallerTag() {
        return callerTag;
    }

    /**
     * @param callerTag the callerTag to set
     */
    public void setCallerTag(String callerTag) {
        this.callerTag = callerTag;
    }
}
