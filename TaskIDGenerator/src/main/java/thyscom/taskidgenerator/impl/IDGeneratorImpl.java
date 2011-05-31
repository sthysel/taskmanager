package thyscom.taskidgenerator.impl;

import org.openide.util.lookup.ServiceProvider;
import thyscom.taskidgenerator.api.IDGenerator;

/**
 *
 * @author thys
 */
@ServiceProvider(service=IDGenerator.class)
public class IDGeneratorImpl implements IDGenerator {

    @Override
    public String getID() {
        return System.currentTimeMillis() + "";
    }
}
