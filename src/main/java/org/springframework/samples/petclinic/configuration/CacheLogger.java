
package org.springframework.samples.petclinic.configuration;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheLogger implements CacheEventListener<Object, Object> {

	private final Logger log = LoggerFactory.getLogger(CacheLogger.class);


	@Override
	public void onEvent(final CacheEvent<?, ?> event) {
		this.log.info("Key: {} | EventType: {} | Old value: {} | New Value: {}", event.getKey(), event.getType(), event.getOldValue(), event.getNewValue());

	}

}
