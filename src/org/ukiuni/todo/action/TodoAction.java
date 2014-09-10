package org.ukiuni.todo.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.arnx.jsonic.JSON;

import org.ukiuni.todo.model.Project;
import org.ukiuni.todo.model.Todo;
import org.ukiuni.todo.model.Todo.Status;
import org.ukiuni.todo.service.ProjectService;
import org.ukiuni.todo.service.TodoService;

@Path("/todo")
public class TodoAction {
	public TodoService todoService = new TodoService();
	public ProjectService projectService = new ProjectService();

	@POST
	@Path("createProject")
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectDto createProject(@FormParam("projectName") String projectName) {
		Project project = projectService.create(projectName);
		ProjectDto dto = new ProjectDto();
		dto.projectKey = project.getKey();
		dto.projectName = project.getName();
		return dto;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public void saveTodos(String body) {
		ProjectDto project = JSON.decode(body, ProjectDto.class);
		Project registedProject = projectService.load(project.projectKey);
		if (null == registedProject) {
			throw new NotFoundException("project not found");
		}
		List<Todo> todolist = new ArrayList<Todo>();
		long todoIndex = 0;
		for (int i = 0; i < project.todos.length; i++) {
			Todo[] todos = project.todos[i];
			for (Todo todo : todos) {
				todo.setStatus(Status.values()[i]);
				if (0 == todo.getId()) {
					todo.setCreatedAt(new Date());
				} else {
					todo.setUpdatedAt(new Date());
				}
				todo.setIndex(todoIndex++);
				todolist.add(todo);
			}
		}
		registedProject.setTodos(todolist);
		projectService.update(registedProject);
	}

	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteTodos(String body) {
		DeleteDodosJsonSchema projectDto = JSON.decode(body, DeleteDodosJsonSchema.class);
		Project project = projectService.load(projectDto.projectKey);
		List<Todo> updateTodoList = new ArrayList<Todo>();
		for (Todo todo : projectDto.todos) {
			todo.setStatus(Status.DELETED);
			todo.setUpdatedAt(new Date());
			todo.setProject(project);
			updateTodoList.add(todo);
		}
		todoService.merge(updateTodoList, project.getTodos());
		todoService.save(updateTodoList.toArray(new Todo[0]));
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ArrayList<Todo>> loadTodo(@QueryParam("project_key") String projectKey) {
		Project project = projectService.load(projectKey);
		if (project == null) {
			throw new NotFoundException();
		}
		List<ArrayList<Todo>> todos = Arrays.asList(new ArrayList<Todo>(), new ArrayList<Todo>(), new ArrayList<Todo>());

		for (Todo todo : project.getTodos()) {
			todo.setProject(null);
			int statusIndex = todo.getStatus().ordinal();
			if (statusIndex < 3) {
				todos.get(statusIndex).add(todo);
			}
		}
		return todos;
	}

	public static class ProjectDto {
		public String projectKey;
		public String projectName;
		public Todo[][] todos;

		@Override
		public String toString() {
			return "ProjectJsonSchema [projectKey=" + projectKey + ", projectName=" + projectName + ", todos=" + Arrays.toString(todos) + "]";
		}
	}

	public static class DeleteDodosJsonSchema {
		public String projectKey;
		public Todo[] todos;
	}

}
