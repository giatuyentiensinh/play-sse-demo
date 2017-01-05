package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;

@Singleton
public class ActorController extends Controller {

	private final ActorSystem actorSystem;
	private final ExecutionContextExecutor exec;

	@Inject
	public ActorController(ActorSystem actorSystem,
			ExecutionContextExecutor exec) {
		super();
		this.actorSystem = actorSystem;
		this.exec = exec;
	}

	public CompletionStage<Result> localHello(String name) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		CompletableFuture<String> future = new CompletableFuture();
		actorSystem.scheduler().scheduleOnce(
				Duration.create(1, TimeUnit.SECONDS),
				() -> future.complete(name), exec);
		return future.thenApplyAsync(Results::ok, exec);

	}

}