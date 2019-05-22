package rmi.communication;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Message implements Serializable {
    private static final long serialVersionUID = 31949772994472956L;
    public String method;       // method to be invoked
    public Object[] args;       // args for the method
    public Object returnValue;  // the return value from the remote

    public Message(String method, Object[] args){
        this.method = method;
        this.args = args;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    /* The parameter of invoke is a Skeleton Object,
        which holds the real interface.*/
    public void invoke(Object obj){
        Method m;
        try {
            if (args != null) {
                Class[] types = new Class[args.length];
                for (int i = 0; i < args.length; ++i) {
                    types[i] = args[i].getClass();
                }
                m = obj.getClass().getMethod(method, types);
            } else {
                m = obj.getClass().getMethod(method);
            }
            // invoke the method and set the returnValue
            returnValue = m.invoke(obj, args);
        }catch (NoSuchMethodException e){
            System.out.println("no such method");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }



}
