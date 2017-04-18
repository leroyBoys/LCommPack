package com;

import com.google.protobuf.InvalidProtocolBufferException;
import com.protobuftest.protobuf.PersonProbuf;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/5.
 */
public class FirstProbuff implements ProbufferInterface<PersonProbuf.Person> {
    @Override
    public PersonProbuf.Person getObject(byte[] bytes) {
        try {
            return PersonProbuf.Person.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}
