import javax.swing.*;           // Import necessary Swing components
import javax.swing.table.DefaultTableModel;  // For table model
import java.awt.*;  // For layout management
import java.awt.event.*;  // For handling events
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.*;   

public class ToDoAppGUI extends JFrame {
    private TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JCheckBox showCompletedCheckBox; // For showing completed tasks
    private JComboBox<String> categoryFilter; // For category filtering
    private JComboBox<String> priorityFilter; // For priority filtering
    private JComboBox<String> dueDateFilter; // For due date filtering
    private JComboBox<String> showCompleted; // For showing completed tasks

    public ToDoAppGUI() {
        taskManager = new TaskManager();
        loadTasksFromFile(); // Load tasks from file on startup
        initGUI();
        updateCategoryFilterOptions(); // Initialize category filter options
        refreshTaskList(); // loads initial tasks into the table
    }

    private void initGUI() {
    setTitle("To-Do List App for Students");
    setSize(800, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // 🟡 Filter Panel
    JPanel topPanel = new JPanel();

    showCompletedCheckBox = new JCheckBox("Show Completed");
    topPanel.add(showCompletedCheckBox);
    showCompletedCheckBox.addActionListener(e -> refreshTaskList());

    topPanel.add(new JLabel("Priority:"));
    String[] priorities = {"All", "Low", "Medium", "High"};
    priorityFilter = new JComboBox<>(priorities);
    topPanel.add(priorityFilter);
    priorityFilter.addActionListener(e -> refreshTaskList());

    topPanel.add(new JLabel("Category:"));
    categoryFilter = new JComboBox<>();
    topPanel.add(categoryFilter);
    categoryFilter.addActionListener(e -> refreshTaskList());

    topPanel.add(new JLabel("Due Date:"));
    dueDateFilter = new JComboBox<>(new String[]{"All", "Today", "This Week", "Overdue"});
    topPanel.add(dueDateFilter);
    dueDateFilter.addActionListener(e -> refreshTaskList());

    // ✅ THIS LINE ADDS THE PANEL TO THE FRAME
    add(topPanel, BorderLayout.NORTH);
    
    // 🔵 Table setup
    String[] columns = {"Title", "Description", "Due Date", "Priority", "Category", "Status"};
    tableModel = new DefaultTableModel(columns, 0);
    taskTable = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(taskTable);
    add(scrollPane, BorderLayout.CENTER);

    // 🔴 Buttons
    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Add Task");
    JButton removeButton = new JButton("Remove Task");
    JButton completeButton = new JButton("Mark Completed");
    JButton saveButton = new JButton("Save Tasks");

    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(completeButton);
    buttonPanel.add(saveButton);
    add(buttonPanel, BorderLayout.SOUTH);

    // Listeners
    addButton.addActionListener(e -> showAddTaskDialog());
    removeButton.addActionListener(e -> deleteSelectedTask());
    completeButton.addActionListener(e -> markTaskCompleted());
    saveButton.addActionListener(e -> saveTasksToFile());

    setVisible(true);
}


    private void showAddTaskDialog() {
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dueDateField = new JTextField("2025-08-10");
        String[] priorities = {"Low", "Medium", "High"};
        JComboBox<String> priorityComboBox = new JComboBox<>(priorities);
        JTextField categoryField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityComboBox);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String desc = descriptionField.getText();
                LocalDate dueDate = LocalDate.parse(dueDateField.getText());
                String priority = (String) priorityComboBox.getSelectedItem();
                Category category = new Category(categoryField.getText());

                Task task = new Task(title, desc, dueDate, priority, category);
                taskManager.addTask(task);
                refreshTaskList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.");
            }
        }
        updateCategoryFilterOptions(); // Update category filter after adding a task
    }

    private void deleteSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row != -1) {
            Task task = getDisplayedTasks().get(row);
            taskManager.removeTask(task);
            refreshTaskList();
        }
    }

    private void markTaskCompleted() {
        int row = taskTable.getSelectedRow();
        if (row != -1) {
            Task task = getDisplayedTasks().get(row);
            task.setCompleted(true);
            refreshTaskList();
        }
    }

    private void refreshTaskList() {
    tableModel.setRowCount(0); // Clear table

    List<Task> tasks = getDisplayedTasks(); // Get filtered list

    for (Task task : tasks) {
        tableModel.addRow(new Object[]{
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getCategory().getName(),
                task.isCompleted() ? "✅ Completed" : "⏳ Pending"
        });
    }
}
 
    private void updateCategoryFilterOptions() {
    categoryFilter.removeAllItems();
    categoryFilter.addItem("All");

    Set<String> categories = taskManager.getAllTasks().stream()
            .map(task -> task.getCategory().getName())
            .collect(Collectors.toSet());

    for (String category : categories) {
        categoryFilter.addItem(category);
    }
}
 
    private void saveTasksToFile() {
    try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.txt"))) {
        for (Task task : taskManager.getAllTasks()) {
            writer.println(task.getTitle() + "|" +
                           task.getDescription() + "|" +
                           task.getDueDate() + "|" +
                           task.getPriority() + "|" +
                           task.getCategory().getName() + "|" +
                           task.isCompleted());
        }
        JOptionPane.showMessageDialog(this, "Tasks saved to tasks.txt");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving tasks: " + e.getMessage());
    }
}

private void loadTasksFromFile() {
    File file = new File("tasks.txt");
    if (!file.exists()) return; // Nothing to load

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length == 6) {
                String title = parts[0];
                String description = parts[1];
                LocalDate dueDate = LocalDate.parse(parts[2]);
                String priority = parts[3];
                Category category = new Category(parts[4]);
                boolean isCompleted = Boolean.parseBoolean(parts[5]);

                Task task = new Task(title, description, dueDate, priority, category);
                task.setCompleted(isCompleted);
                taskManager.addTask(task);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage());
    }
}



    // Utility method to get displayed tasks matching filter
    private List<Task> getDisplayedTasks() {
    String selectedPriority = (String) priorityFilter.getSelectedItem();
    String selectedCategory = (String) categoryFilter.getSelectedItem();
    String selectedDueDate = (String) dueDateFilter.getSelectedItem();
    boolean showCompletedOnly = showCompletedCheckBox.isSelected();

    LocalDate today = LocalDate.now();

    return taskManager.getAllTasks().stream()
            .filter(task -> {
                // Filter by priority
                if (!selectedPriority.equals("All") &&
                        !task.getPriority().equalsIgnoreCase(selectedPriority)) {
                    return false;
                }

                // Filter by category
                if (selectedCategory != null && !selectedCategory.equals("All") &&
                        !task.getCategory().getName().equalsIgnoreCase(selectedCategory)) {
                    return false;
                }

                // Filter by completion status
                if (showCompletedOnly && !task.isCompleted()) {
                    return false;
                }

                // Filter by due date
                if (selectedDueDate != null && !selectedDueDate.equals("All")) {
                    LocalDate dueDate = task.getDueDate();
                    switch (selectedDueDate) {
                        case "Today":
                            if (!dueDate.equals(today)) return false;
                            break;
                        case "This Week":
                            LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
                            LocalDate endOfWeek = startOfWeek.plusDays(6);
                            if (dueDate.isBefore(startOfWeek) || dueDate.isAfter(endOfWeek)) return false;
                            break;
                        case "Overdue":
                            if (!dueDate.isBefore(today)) return false;
                            break;
                    }
                }

                return true;
            })
            .collect(Collectors.toList());
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoAppGUI::new);
    }
}
