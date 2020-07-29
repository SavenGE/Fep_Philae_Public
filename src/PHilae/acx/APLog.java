/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author Pecherk
 */
public abstract class APLog implements Serializable {

    private String indent = "\t";

    public abstract void logDebug(Object event);

    public abstract void logDebug(String key, Object event);

    public abstract void logDebug(String key, Object event, String prefix);

    public abstract void logEvent(Object event);

    public abstract void logEvent(Object input, Object event);

    public abstract void logError(Throwable ex);

    public abstract void setCall(String callRef, Object callObject);

    public abstract void setCall(String callRef, Object callObject, boolean replace);

    public abstract void setCall(String callRef, Object callObject, String prefix);

    public abstract void setCall(String callRef, Object callObject, long position);

    public abstract void setCall(String callRef, Object callObject, boolean replace, long position);

    public abstract void setCall(String callRef, Object callObject, boolean replace, long position, String prefix);

    public String convertToString(Object object) {
        return convertToString(new ArrayList<>(), object, null).trim();
    }

    private String convertToString(ArrayList parents, Object object, String tag) {
        tag = decapitalize(tag);
        String text = "", items = "", prevVal = "";
        Class<?> beanClass = !isBlank(object) ? object.getClass() : String.class;
        try {
            if (isBlank(object)) {
                return !isBlank(tag) ? (tag + "=<" + String.valueOf(object) + ">") : String.valueOf(object);
            }
            if (object instanceof JAXBElement) {
                return convertToString(parents, extractValue((JAXBElement) object), tag);
            }
            for (MethodDescriptor methodDescriptor : Introspector.getBeanInfo(beanClass).getMethodDescriptors()) {
                if ("toString".equalsIgnoreCase(methodDescriptor.getName()) && beanClass == methodDescriptor.getMethod().getDeclaringClass() && methodDescriptor.getMethod().getAnnotation(AXIgnore.class) == null) {
                    return !isBlank(tag) ? (tag + "=<" + String.valueOf(object) + ">") : String.valueOf(object);
                }
            }
            if (object instanceof byte[]) {
                return !isBlank(tag) ? (tag + "=<" + new String((byte[]) object) + ">") : new String((byte[]) object);
            }

            tag = isBlank(tag) ? beanClass.getSimpleName() : tag;

            if (object instanceof Collection) {
                for (Object item : ((Collection) object).toArray()) {
                    items += (isBlank(items) ? "" : ", ") + (prevVal.contains("\r\n") ? "\r\n" : "") + (prevVal = convertToString(parents, item, null)).trim();
                }
                return items.contains("\r\n") ? ("\r\n" + tag + "=<[" + "\r\n\t" + items + "\r\n" + "]>") : (tag + "=<[" + (!isBlank(items) ? " " + items + " " : "") + "]>");
            } else if (object instanceof Map) {
                for (Object key : ((Map) object).keySet()) {
                    items += (isBlank(items) ? "" : ", ") + (prevVal.contains("\r\n") ? "\r\n" : "") + (prevVal = convertToString(parents, ((Map) object).get(key), String.valueOf(key))).trim();
                }
                return items.contains("\r\n") ? ("\r\n" + tag + "=<[" + "\r\n\t" + items + "\r\n" + "]>") : (tag + "=<[" + (!isBlank(items) ? " " + items + " " : "") + "]>");
            } else if (beanClass.isArray()) {
                switch (beanClass.getSimpleName()) {
                    case "int[]":
                        return (tag + "=<" + Arrays.toString((int[]) object) + ">");
                    case "long[]":
                        return (tag + "=<" + Arrays.toString((long[]) object) + ">");
                    case "boolean[]":
                        return (tag + "=<" + Arrays.toString((boolean[]) object) + ">");
                    case "byte[]":
                        return (tag + "=<" + Arrays.toString((byte[]) object) + ">");
                    case "char[]":
                        return (tag + "=<" + Arrays.toString((char[]) object) + ">");
                    case "double[]":
                        return (tag + "=<" + Arrays.toString((double[]) object) + ">");
                    case "float[]":
                        return (tag + "=<" + Arrays.toString((float[]) object) + ">");
                    case "short[]":
                        return (tag + "=<" + Arrays.toString((short[]) object) + ">");
                }
                if (object instanceof Object[]) {
                    for (Object item : (Object[]) object) {
                        items += (isBlank(items) ? "" : ", ") + (prevVal.contains("\r\n") ? "\r\n\t" : "") + (prevVal = convertToString(parents, item, null)).trim();
                    }
                    return items.contains("\r\n") ? ("\r\n" + tag + "=<[" + "\r\n\t" + items + "\r\n" + "]>") : (tag + "=<[" + (!isBlank(items) ? " " + items + " " : "") + "]>");
                } else {
                    return (tag + "=<[" + String.valueOf(object) + "]>");
                }
            } else {
                Method readMethod;
                parents.add(beanClass);
                for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(beanClass).getPropertyDescriptors()) {
                    if ((readMethod = propertyDesc.getReadMethod()) != null) {
                        Object value = "@unknown";
                        try {
                            value = readMethod.invoke(object);
                        } catch (Exception ex) {
                            ex = null;
                        }
                        if (!(value instanceof Class)) {
                            text += (isBlank(text) ? "" : ", ") + convertToString(new ArrayList(parents), !parents.contains(value != null ? value.getClass() : null) ? value : String.valueOf(value), propertyDesc.getName());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logError(ex);
        }
        return (!isBlank(tag) ? (tag + "=<[ " + (isBlank(text) ? String.valueOf(object) : text) + " ]>") : (isBlank(text) ? String.valueOf(object) : text));
    }

    public void printError(StringBuilder logEvent, Throwable event, Object input, String message, String tag, String preIndent) {
        logEvent.append("\r\n").append(preIndent).append("<").append(tag).append(">");
        logEvent.append("\r\n").append(preIndent).append(getIndent()).append("<class>").append((event).getClass().getSimpleName()).append("</class>");
        if (input != null) {
            logEvent.append("\r\n").append(preIndent).append(getIndent()).append("<input>").append(String.valueOf(input)).append("</input>");
        }
        logEvent.append("\r\n").append(preIndent).append(getIndent()).append("<message>").append(message == null ? "" : message).append("[ ").append(cleanText((event).getMessage())).append(" ]").append("</message>");
        logEvent.append("\r\n").append(preIndent).append(getIndent()).append("<stacktrace>");
        for (StackTraceElement s : (event).getStackTrace()) {
            logEvent.append("\r\n").append(preIndent).append(getIndent()).append(getIndent()).append("at ").append(s.toString());
        }
        Throwable cause = event.getCause();
        if (cause != null) {
            printError(logEvent, cause, input, message, "cause", preIndent + getIndent() + getIndent());
        }
        logEvent.append("\r\n").append(preIndent).append(getIndent()).append("</stacktrace>");
        logEvent.append("\r\n").append(preIndent).append("</").append(tag).append(">");
    }

    public String indentLines(String text, String lineIndent) {
        String line, buffer = "";
        try (BufferedReader bis = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())))) {
            while ((line = bis.readLine()) != null) {
                buffer += lineIndent + line + "\r\n";
            }
        } catch (Exception ex) {
            return buffer;
        }
        return lineIndent + buffer.trim();
    }

    public String cleanText(String text) {
        String line;
        StringBuilder buffer = new StringBuilder();
        if (!isBlank(text)) {
            try (BufferedReader bis = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())))) {
                while ((line = bis.readLine()) != null) {
                    buffer.append(line.trim());
                }
            } catch (Exception ex) {
                ex = null;
            }
        }
        return buffer.toString().replaceAll(">\\s+<", "><");
    }

    public String capitalize(String text, boolean convertAllXters) {
        if (text != null && text.length() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String word : text.replace("_", " ").split("\\s")) {
                builder.append(word.length() > 2 ? word.substring(0, 1).toUpperCase() + (convertAllXters ? word.substring(1).toLowerCase() : word.substring(1)) : word.toLowerCase()).append(" ");
            }
            return builder.toString().trim();
        }
        return text;
    }

    public String decapitalize(String text) {
        if (text != null && text.length() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String word : text.split("\\s")) {
                builder.append(word.substring(0, 1).toLowerCase()).append(word.substring(1)).append(" ");
            }
            return builder.toString().trim();
        }
        return text;
    }

    public boolean isBlank(Object object) {
        return object == null || "".equals(String.valueOf(object).trim()) || "null".equals(String.valueOf(object).trim());
    }

    public <T> T extractValue(JAXBElement<T> element) {
        return !isBlank(element) ? (T) element.getValue() : null;
    }

    public String indentLines(String text) {
        return indentLines(text, getIndent());
    }

    public String capitalize(String text) {
        return capitalize(text, true);
    }

    public String checkBlank(String value) {
        return APLog.this.checkBlank(value, "");
    }

    public <T> T checkBlank(T value, T nillValue) {
        return !isBlank(value) ? value : nillValue;
    }

    public void setAlert(String alertCode) {
        setCall("alert", checkBlank(alertCode), 1);
    }

    public void setTxnId(String reference) {
        setCall("txnId", checkBlank(reference), 2);
    }

    public void setTxnType(String txnType) {
        setCall("type", checkBlank(txnType), 3);
    }

    public void setAccount(String account) {
        setCall("account", checkBlank(account), 4);
    }

    public void setNarration(String narration) {
        setCall("narration", checkBlank(narration), 5);
    }

    public void setReversal(boolean reversal) {
        setCall("reversal", yesNo(reversal));
    }

    public void setMessage(Object message) {
        setCall("message", message);
    }

    public void setRecord(Object record) {
        setCall("record", record);
    }

    public void setRequest(Object request) {
        setCall("request", request, 6);
    }

    public void setResponse(Object response) {
        setCall("response", response);
    }

    public void setResult(String result) {
        setCall("result", checkBlank(result));
    }

    public void setDuration(String duration) {
        setCall("duration", checkBlank(duration));
    }

    public String yesNo(boolean isYes) {
        return isYes ? "Yes" : "No";
    }

    /**
     * @return the indent
     */
    public String getIndent() {
        return indent;
    }

    /**
     * @param indent the indent to set
     */
    public void setIndent(String indent) {
        this.indent = indent;
    }
}
