package controllers;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joda.time.LocalDate;

import play.Logger;
import play.libs.EventSource;
import play.libs.EventSource.Event;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.MyInstance;
import services.SSEPublisher;
import akka.NotUsed;
import akka.stream.javadsl.Source;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author tuyenng
 *
 */
@Singleton
public class SSEController extends Controller {

	private final MyInstance instance;

	@Inject
	private SSEPublisher publisher;

	@Inject
	public SSEController(MyInstance instance) {
		super();
		this.instance = instance;
	}

	/**
	 * Test increment count
	 * <p>
	 * count += 2
	 * </p>
	 * 
	 * @return count
	 */
	public Result index() {
		return ok(Integer.toString(instance.nextCount()));
	}

	/**
	 * Test Json data
	 * 
	 * @return json node
	 */
	public Result showJson() {
		ObjectNode node = Json.newObject();
		node.put("name", "Tuyenng");
		node.put("age", 22);
		node.put("count", instance.nextCount());
		return ok(node);
	}

	/**
	 * Send message event
	 * 
	 * @param msg
	 *            data
	 * @return
	 */
	public Result send(String msg) {
		ObjectNode node = Json.newObject();
		node.put("address", request().remoteAddress());
		node.put("count", instance.nextCount());
		node.put("time", new LocalDate().toString("dd/MM/yyyy"));
		node.put("msg", msg);
		publisher.publish(node, "tuyenng");
		return ok();
	}

	/**
	 * Register event
	 * 
	 * @return chunked
	 */
	public Result realTime() {
		final Source<Event, NotUsed> eventSource = Source
				.fromPublisher(publisher);
		return ok().chunked(eventSource.via(EventSource.flow())).as(
				Http.MimeTypes.EVENT_STREAM);
	}
}
