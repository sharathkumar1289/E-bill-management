package com.spc.utility;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Date now = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String id = dateFormat.format(now);

        return Long.valueOf(id);
    }
}
