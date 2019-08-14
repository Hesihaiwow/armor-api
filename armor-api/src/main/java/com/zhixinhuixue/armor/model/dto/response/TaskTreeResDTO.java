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
    private String treeName;
    private Integer level;
    private Long id;

    /**
     * 是否编辑
     */
    private boolean isEdit;

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * 功能点
     */
    private List<TestFunctionTreeResDTO> functionTreeResDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

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

    public List<TestFunctionTreeResDTO> getFunctionTreeResDTOS() {
        return functionTreeResDTOS;
    }

    public void setFunctionTreeResDTOS(List<TestFunctionTreeResDTO> functionTreeResDTOS) {
        this.functionTreeResDTOS = functionTreeResDTOS;
    }

    public static class TestFunctionTreeResDTO{
        /**
         * 父级id
         */
        private Long pid;

        /**
         * 功能点
         */
        private Long functionId;
        private String function;
        private String treeName;
        private Integer level;
        private Long id;

        /**
         * 是否编辑
         */
        private boolean isEdit;

        public boolean getIsEdit() {
            return isEdit;
        }

        public void setIsEdit(boolean isEdit) {
            this.isEdit = isEdit;
        }

        public String getTreeName() {
            return treeName;
        }

        public void setTreeName(String treeName) {
            this.treeName = treeName;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }

        /**
         * 测试用例
         */
        private List<TestExampleTreeResDTO> functionTreeResDTOS;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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

        public List<TestExampleTreeResDTO> getFunctionTreeResDTOS() {
            return functionTreeResDTOS;
        }

        public void setFunctionTreeResDTOS(List<TestExampleTreeResDTO> functionTreeResDTOS) {
            this.functionTreeResDTOS = functionTreeResDTOS;
        }

        public static class TestExampleTreeResDTO{
            /**
             * 父级id
             */
            private Long pid;

            public Long getPid() {
                return pid;
            }

            public void setPid(Long pid) {
                this.pid = pid;
            }

            /**
             * 测试用例
             */
            private Long id;
            private String name;
            private String treeName;
            private Integer level;

            /**
             * 是否编辑
             */
            private boolean isEdit;

            public boolean getIsEdit() {
                return isEdit;
            }

            public void setIsEdit(boolean isEdit) {
                this.isEdit = isEdit;
            }

            public String getTreeName() {
                return treeName;
            }

            public void setTreeName(String treeName) {
                this.treeName = treeName;
            }

            public Integer getLevel() {
                return level;
            }

            public void setLevel(Integer level) {
                this.level = level;
            }

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
