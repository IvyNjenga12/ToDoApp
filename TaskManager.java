import java.util.ArrayList;          // Import necessary classes
import java.util.HashMap;           // For storing tasks by category
import java.util.List;             // For storing all tasks
import java.util.Map;              // For storing tasks by category
import java.util.stream.Collectors;// For filtering tasks
import java.time.LocalDate;       // For handling dates

public class TaskManager {

    private List<Task> tasks;        // List to hold all tasks
    private Map<String, List<Task>> tasksByCategory; // Map to hold tasks by category

    public TaskManager() {
        tasks = new ArrayList<>(); // Initialize the task list
        tasksByCategory = new HashMap<>(); // Initialize the map for categories
    }

    public void addTask(Task task) {
        tasks.add(task); // Add task to the list
        tasksByCategory.computeIfAbsent(task.getCategory().getName(), k -> new ArrayList<>()).add(task); // Add task to the category map
    }

    public void removeTask(Task task) {
        tasks.remove(task); // Remove task from the list
        List<Task> categoryTasks = tasksByCategory.get(task.getCategory().getName());
        if (categoryTasks != null) {
            categoryTasks.remove(task); // Remove task from the category list
            if (categoryTasks.isEmpty()) {
                tasksByCategory.remove(task.getCategory().getName()); // Remove category if no tasks left
            }
        }
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task task = tasks.get(index);
            removeTask(task); // Use the other remove method
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks); // Return a copy of the task list
    }

    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isCompleted) // Filter completed tasks
                .collect(Collectors.toList()); // Collect to a list
    }

    public List<Task> getTasksByCategory(String categoryName) {
        return tasksByCategory.getOrDefault(categoryName, new ArrayList<>()); // Return tasks for the specified category
    }

    public List<Task> getTasksByPriority(String priority) {
        return tasks.stream()
                .filter(task -> task.getPriority().equalsIgnoreCase(priority)) // Filter by priority
                .collect(Collectors.toList()); // Collect to a list
    }
    public List<Task> getTasksDueBefore(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.getDueDate().isBefore(date)) // Filter tasks due before the specified date
                .collect(Collectors.toList()); // Collect to a list         
    }
    public List<Task> getTasksDueToday() {
        LocalDate today = LocalDate.now(); // Get today's date
        return tasks.stream()
                .filter(task -> task.getDueDate().isEqual(today)) // Filter tasks due today
                .collect(Collectors.toList()); // Collect to a list
    }
    public List<Task> getTasksDueThisWeek() {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY); // Start of the week
        LocalDate endOfWeek = startOfWeek.plusDays(6); // End of the week
        return tasks.stream()
                .filter(task -> !task.getDueDate().isBefore(startOfWeek) && !task.getDueDate().isAfter(endOfWeek)) // Filter tasks due this week
                .collect(Collectors.toList()); // Collect to a list
    }

    
}
