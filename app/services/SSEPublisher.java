package services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.EventSource;
import play.libs.EventSource.Event;
import play.libs.Json;

/**
 * @author tuyenng
 *
 */
public class SSEPublisher implements Publisher<EventSource.Event> {

	List<Subscriber<? super Event>> subscriberList = new ArrayList<>();

	@Inject
	public SSEPublisher() {
	}

	@Override
	public void subscribe(Subscriber<? super Event> s) {
		subscriberList.add(s);
	}

	public void publish(JsonNode nodeMessage, String id) {
		for (Subscriber<? super Event> subscriber : subscriberList) {
			subscriber.onNext(new Event(Json.stringify(nodeMessage), id,
					"messageTuyenng"));
		}
	}
}
