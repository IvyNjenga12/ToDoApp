import java.time.LocalDate;

public class Task {
    private String title;              // title of the task
    private String description;        // description of the task
    private LocalDate createdDate;     // when the task was created
    private LocalDate dueDate;         // due date of the task
    private String priority;           // e.g., "Low", "Medium", "High"
    private Category category;         // task category
    private String status;             // e.g., "Pending", "In Progress", "Completed"
    private boolean isCompleted;       // true if task is done

    // Constructor
    public Task(String title, String description, LocalDate dueDate, String priority, Category category) {
        this.title = title;
        this.description = description;
        this.createdDate = LocalDate.now();   // auto-set to current date
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.status = "Pending";              // default status
        this.isCompleted = false;             // default completion state
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getCreatedDate() { return createdDate; }
    public LocalDate getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public Category getCategory() { return category; }
    public String getStatus() { return status; }
    public boolean isCompleted() { return isCompleted; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setCategory(Category category) { this.category = category; }
    public void setStatus(String status) { this.status = status; }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
        this.status = completed ? "Completed" : "Pending";  // Update status based on completion
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", dueDate=" + dueDate +
                ", priority='" + priority + '\'' +
                ", category=" + category +
                ", status='" + status + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
