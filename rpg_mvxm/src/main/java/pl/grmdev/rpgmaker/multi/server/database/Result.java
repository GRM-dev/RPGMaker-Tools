/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.database;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;

/**
 * @author Levvy055
 *
 */
public class Result {

	private boolean success;
	private boolean error;
	private String msg;
	
	/**
	 * @param success
	 * @param msg
	 */
	private Result(boolean success, boolean error, String msg) {
		this.success = success;
		if (success) {
			this.msg = msg;
		}
		this.error = error;
	}
	
	public static Response json(String json) {
		return Response.ok(json).build();
	}
	
	public static Response success(String info) {
		if (info == null || info.isEmpty()) {
			return Response.ok().build();
		}
		return Response.ok(new Result(true, false, info).asJson()).build();
	}
	
	public static Response noAuth(boolean error, String obj) {
		return getStatusResponse(error, obj, Status.UNAUTHORIZED);
	}

	public static Response notFound(boolean error, String obj) {
		return getStatusResponse(error, obj, Status.NOT_FOUND);
	}
	
	/**
	 * @param b
	 * @param msg
	 * @return
	 */
	public static Response created(boolean warning, String msg) {
		return Response.status(Status.CREATED).entity(new Result(true, warning, msg).asJson()).build();
	}
	
	private static Response getStatusResponse(boolean error, String obj, Status status) {
		ResponseBuilder respB = Response.status(status);
		if (error) {
			respB = respB.entity(new Result(false, true, obj).asJson());
		} else if (obj != null) {
			respB = respB.entity(obj);
		}
		return respB.build();
	}

	public static Response exception(Exception e) {
		return Result.exception(e, null);
	}
	
	public static Response exception(Exception e, String info) {
		return Response.status(500).entity(new Result(false, true, info + "\n" + e.getMessage()).asJson()).build();
	}
	
	/**
	 * @return
	 */
	public String asJson() {
		return toString();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"success\":\"");
		builder.append(success);
		builder.append("\",\"error\":\"");
		builder.append(error);
		builder.append("\",\"");
		if (msg != null) {
			if (error) {
				builder.append("errorMsg\":\"");
			} else {
				builder.append("message\":\"");
				builder.append("\"" + msg + "\"");
				builder.append("\",\"");
			}
		}
		builder.append("\"} ");
		return builder.toString();
	}
}
