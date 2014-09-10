package org.ukiuni.todo.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Todo {
	@Id
	@GeneratedValue
	private long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Project project;
	private String title;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Enumerated(EnumType.STRING)
	private Status status;
	private long index;

	public Todo() {
	}

	public Todo(String title, String description) {
		this.title = title;
		this.description = description;
		this.createdAt = new Date();
		this.status = Status.TODO;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public static enum Status {
		TODO, DOING, DONE, DELETED
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", project=" + project + ", title=" + title + ", description=" + description + ", updatedAt=" + updatedAt + ", createdAt=" + createdAt + ", status=" + status + ", index=" + index + "]";
	}

}
