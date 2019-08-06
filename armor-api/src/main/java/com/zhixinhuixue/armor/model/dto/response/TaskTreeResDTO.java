package com.zhixinhuixue.armor.model.dto.response;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/8/6 14:40
 *
 * 测试用例树
 */
public class TaskTreeResDTO {
    /**
     * 任务
     */
    private Long taskId;
    private String taskName;

    /**
     * 功能点
     */
    private List<TaskFunctionTreeResDTO> functionTreeResDTOS;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<TaskFunctionTreeResDTO> getFunctionTreeResDTOS() {
        return functionTreeResDTOS;
    }

    public void setFunctionTreeResDTOS(List<TaskFunctionTreeResDTO> functionTreeResDTOS) {
        this.functionTreeResDTOS = functionTreeResDTOS;
    }

    public static class TaskFunctionTreeResDTO{
        /**
         * 功能点
         */
        private Long functionId;
        private String function;


        /**
         * 测试用例
         */
        private List<TestExampleTreeResDTO> exampleTreeResDTOS;

        public Long getFunctionId() {
            return functionId;
        }

        public void setFunctionId(Long functionId) {
            this.functionId = functionId;
        }

        public String getFunction() {
            return function;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public List<TestExampleTreeResDTO> getExampleTreeResDTOS() {
            return exampleTreeResDTOS;
        }

        public void setExampleTreeResDTOS(List<TestExampleTreeResDTO> exampleTreeResDTOS) {
            this.exampleTreeResDTOS = exampleTreeResDTOS;
        }

        public static class TestExampleTreeResDTO{
            /**
             * 测试用例
             */
            private Long id;
            private String name;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
