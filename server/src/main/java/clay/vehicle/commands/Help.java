package clay.vehicle.commands;

/**
 * Command implementation for displaying help information. This command provides information about
 * available commands and their usage.
 */
public class Help implements Executable {

  /**
   * Executes the help command. Returns a string containing help information about * available
   * commands.
   *
   * @param args command arguments (not used)
   * @return help information as a string
   */
  @Override
  public String execute(String[] args) {
    return """
                help: вывести справку по доступным командам
                info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                insert {element}: добавить новый элемент с заданным ключом
                update id {element}: обновить значение элемента коллекции, id которого равен заданному
                remove_key id: удалить элемент из коллекции по его ключу
                clear: очистить коллекцию
                execute_script file_name: считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                exit: завершить клиент
                remove_lower {element}: удалить из коллекции все элементы, меньшие, чем заданный
                replace_if_greater id {element}: заменить значение по ключу, если новое значение больше старого
                remove_lower_key id: удалить из коллекции все элементы, ключ которых меньше, чем заданный
                remove_any_by_engine_power enginePower: удалить из коллекции один элемент, значение поля enginePower которого эквивалентно заданному
                group_counting_by_coordinates: сгруппировать элементы коллекции по значению поля coordinates, вывести количество элементов в каждой группе
                print_ascending: вывести элементы коллекции в порядке возрастания

                Скрипты:
                строки, начинающиеся на ";" рассматриваются как комментарии, игнорируются при исполнении
                строки, начинающиеся на ": " обозначают ввод данных пользователем

                Ниже приведён пример скрипта:
                info
                show

                ; arguments test
                insert
                : 2009 Toyota Yaris Gray
                : 8
                : 4
                : 23
                : 9
                :
                : alcohol
                show
                """;
  }
}
