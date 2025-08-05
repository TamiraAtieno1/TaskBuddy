/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskbuddy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Generates a unique String ID for a new task.
     * @return a unique String ID.
     */
    private String generateTaskId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a unique String ID for a new category.
     * @return a unique String ID.
     */
    public String generateCategoryId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Adds a new task to the list.
     */
    public void addTask(String title, String description, LocalDate dueDate, String priority, String status, Category category) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Error: Task title cannot be empty.");
            return;
        }
        if (dueDate == null) {
            System.out.println("Error: Due date cannot be null.");
            return;
        }
        if (status == null || status.trim().isEmpty()) {
            System.out.println("Error: Status cannot be empty");
            return;
        }
        if (priority == null || priority.trim().isEmpty()) {
            System.out.println("Error: Priority cannot be empty");
            return;
        }
        if (category == null) {
            System.out.println("Error: Category cannot be null");
            return;
        }

        Task newTask = new Task(generateTaskId(), title, description, dueDate, priority, status, category);
        tasks.add(newTask);
        System.out.println("Task added: " + newTask.getTitle());
    }
    
    /**
     * Removes a task by its String ID.
     */
    public void removeTask(String taskId) {
        boolean removed = tasks.removeIf(task -> task.getTaskId().equals(taskId));
        if (removed) {
            System.out.println("Task with ID " + taskId + " removed.");
        } else {
            System.out.println("Error: Task with ID " + taskId + " not found.");
        }
    }

    /**
     * Updates an existing task by its String ID.
     */
    public void updateTask(String taskId, String title, String description, LocalDate dueDate, String priority, String status, Category category) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Error: Task title cannot be empty during update.");
            return;
        }
        if (dueDate == null) {
            System.out.println("Error: Due date cannot be null during update.");
            return;
        }
        if (priority == null || priority.trim().isEmpty()) {
            System.out.println("Error: Priority cannot be empty during update.");
            return;
        }
        if (status == null || status.trim().isEmpty()) {
            System.out.println("Error: Status cannot be empty during update");
            return;
        }
        if (category == null) {
            System.out.println("Error: Category cannot be null during update");
            return;
        }
        
        for (Task task : tasks) {
            if (task.getTaskId().equals(taskId)) {
                task.setTitle(title);
                task.setDescription(description);
                task.setDueDate(dueDate);
                task.setPriority(priority);
                task.setStatus(status);
                task.setCategory(category);
                System.out.println("Task with ID " + taskId + " updated.");
                return;
            }
        }
        System.out.println("Error: Task with ID " + taskId + " not found for update.");
    }
    
    /**
     * Get all tasks.
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    /**
     * A utility method to get a list of all tasks.
     * This is an alias for getAllTasks() for better clarity in the GUI.
     */
    public List<Task> listAllTasks() {
        return getAllTasks();
    }
    
    /**
     * Sort tasks by due date.
     */
    public List<Task> sortTasksByDueDate() {
        return tasks.stream()
            .sorted(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(LocalDate::compareTo)))
            .collect(Collectors.toList());
    }

    /**
     * Sort tasks by priority (e.g., High, Medium, Low).
     */
    public List<Task> sortTasksByPriority() {
        Comparator<Task> priorityComparator = (task1, task2) -> {
            int p1 = getPriorityOrder(task1.getPriority());
            int p2 = getPriorityOrder(task2.getPriority());
            return Integer.compare(p1, p2);
        };
        return tasks.stream()
            .sorted(priorityComparator)
            .collect(Collectors.toList());
    }

    private int getPriorityOrder(String priority) {
        if (priority == null) return Integer.MAX_VALUE;
        return switch (priority.toLowerCase()) {
            case "high" -> 1;
            case "medium" -> 2;
            case "low" -> 3;
            default -> Integer.MAX_VALUE;
        };
    }
    
    /**
     * Filter tasks by category.
     */
    public List<Task> filterTasksByCategory(Category category) {
        if (category == null) {
            System.out.println("Error: Category for filtering cannot be null");
            return new ArrayList<>();
        }
        return tasks.stream()
            .filter(task -> task.getCategory() != null && task.getCategory().getCategoryId().equals(category.getCategoryId()))
            .collect(Collectors.toList());
    }
    
    /**
     * Filter tasks by completion status.
     */
    public List<Task> filterTasksByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            System.out.println("Error: Status for filtering cannot be empty.");
            return new ArrayList<>();
        }
        return tasks.stream()
            .filter(task -> task.getStatus() != null && task.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }
    
    /**
     * Mark a task as completed by its String ID.
     */
    public void markTaskAsCompleted(String taskId) {
        for (Task task : tasks) {
            if (task.getTaskId().equals(taskId)) {
                task.setStatus("Completed");
                System.out.println("Task with ID " + taskId + " marked as Completed.");
                return;
            }
        }
        System.out.println("Error: Task with ID " + taskId + " not found to mark as completed.");
    }
    
    /**
     * View completed tasks separately.
     */
    public List<Task> getCompletedTasks() {
        return filterTasksByStatus("Completed");
    }
    
    /**
     * View incomplete tasks.
     */
    public List<Task> getIncompleteTasks() {
        return tasks.stream()
            .filter(task -> task.getStatus() != null && !task.getStatus().equalsIgnoreCase("Completed"))
            .collect(Collectors.toList());
    }
}
