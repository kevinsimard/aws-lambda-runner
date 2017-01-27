package com.kevinsimard.lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unused")
public final class LocalRunner<I, O> {

    public static <I, O> void main(final String[] args) throws Exception {
        Context context = new Context() {
            public String getAwsRequestId() {
                return null;
            }

            public String getLogGroupName() {
                return null;
            }

            public String getLogStreamName() {
                return null;
            }

            public String getFunctionName() {
                return null;
            }

            public String getFunctionVersion() {
                return null;
            }

            public String getInvokedFunctionArn() {
                return null;
            }

            public CognitoIdentity getIdentity() {
                return null;
            }

            public ClientContext getClientContext() {
                return null;
            }

            public int getRemainingTimeInMillis() {
                return 0;
            }

            public int getMemoryLimitInMB() {
                return 0;
            }

            public LambdaLogger getLogger() {
                return null;
            }
        };

        if (args.length != 2) {
            throw new RuntimeException("Invalid number of arguments");
        }

        Object object;
        String handlerClass = args[0];

        try {
            Class<?> clazz = Class.forName(handlerClass);
            Constructor<?> constructor = clazz.getConstructor();
            object = constructor.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(handlerClass + " not found in classpath");
        }

        if (! (object instanceof RequestHandler)) {
            throw new RuntimeException(
                "Request handler class does not implement " + RequestHandler.class + " interface"
            );
        }

        @SuppressWarnings("unchecked")
        RequestHandler<I, O> requestHandler = (RequestHandler<I, O>) object;

        I requestObject = getRequestObject(requestHandler, args[1]);

        try {
            O output = requestHandler.handleRequest(requestObject, context);
            System.out.println(new ObjectMapper().writeValueAsString(output));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static <I> I getRequestObject(RequestHandler handler, String json) throws IOException {
        Type requestType = null;

        for (Type genericInterface : handler.getClass().getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType) {
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                requestType = genericTypes[0];
            }
        }

        if (null == requestType) return null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructType(requestType));
        } catch (RuntimeException e) {
            return mapper.readValue("{}", mapper.getTypeFactory().constructType(requestType));
        }
    }
}
