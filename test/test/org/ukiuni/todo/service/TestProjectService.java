package test.org.ukiuni.todo.service;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.ukiuni.todo.model.Project;
import org.ukiuni.todo.model.Todo;
import org.ukiuni.todo.service.ProjectService;

public class TestProjectService {

	@Test
	public void testCreate() {
		String projectName = "myProjedtName " + UUID.randomUUID();
		Project project = new ProjectService().create(projectName);
		Assert.assertTrue("registed and id is generated id", project.getId() > 0);
		Assert.assertNotNull("registed and id is generated key", project.getKey());
	}

	@Test
	public void testSave() {
		String projectName = "myProjedtName " + UUID.randomUUID();
		Project project = new ProjectService().create(projectName);
		ProjectService projectService = new ProjectService();
		for (int i = 0; i < 10; i++) {
			project.addTodo(new Todo("title" + i, "description" + i));
		}
		projectService.update(project);
		Assert.assertTrue("registed and id is generated", project.getId() > 0);
	}

}
