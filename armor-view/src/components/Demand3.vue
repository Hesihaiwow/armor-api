<template>
    <div class="demand-con">
        <div>
            <el-tabs @tab-click="handleClick" active-name="new" style="position:relative;margin-bottom: 20px;">
                <el-tab-pane label="新需求" name="new" >
                    <div class="demand-top clearfix">
                        <div class="clearfix">
                            <div class="demand-top-list fl">
                                <span class="ttl-name">优先级:</span>
                                <el-select clearable filterable  v-model="priority" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in prioritys" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">类型:</span>
                                <el-select clearable filterable  v-model="type" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in types" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">读取状态:</span>
                                <el-select clearable filterable  v-model="readStatus" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in readStatuses" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                                <el-select clearable filterable  v-model="origin" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select0">查询</el-button>
                            <el-button type="primary" size="small" @click="newDemandVisible=true,clearDemandForm()" v-show="permit">提需求</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column prop="no" label="序号" align="center" width="70" type="index">
                        </el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <!--<a style="color:#20a0ff;cursor: pointer;" @click.stop.prevent="demandDetail(scope.row.id)">{{scope.row.title}}</a>-->
                                <router-link :to="{path:'demandDetail', query:{id:scope.row.id,status:scope.row.status}}" style="color:#20a0ff;">{{scope.row.title}}</router-link>
                            </template>
                        </el-table-column>
                        <el-table-column  label="类型" align="center" width="100">
                            <template scope="scope">
                                <span v-if="scope.row.type == 0">个人建议</span>
                                <span v-else-if="scope.row.type == 1">市场反馈</span>
                                <span v-else-if="scope.row.type == 2">公司决策</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="优先级" align="center" width="100">
                            <template scope="scope">
                                <div type="text" v-for="item in prioritys" v-if="item.id == scope.row.priority">{{item.name}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.feedbackTime| formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="期待上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.releaseTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="点赞数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.likesNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="状态" align="center" >
                            <template scope="scope">
                                <div type="text" v-for="item in readStatuses" v-if="item.id == scope.row.readStatus">{{item.name}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" v-if="permit" width="150" fixed="right">
                            <template scope="scope">
                                <a style="color:#20a0ff;cursor: pointer;" @click="editDemandVisible=true,editDemand(scope.row)">编辑</a>
                                <a style="color:#20a0ff;cursor: pointer;" @click="deleteDemand(scope.row.id)">删除</a>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange"
                                :current-page.sync="pageNum0"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="进行中" name="running">
                    <div class="demand-top clearfix">
                        <div class="clearfix">
                            <div class="demand-top-list fl">
                                <span class="ttl-name">优先级:</span>
                                <el-select clearable filterable  v-model="priority1" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in prioritys" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">类型:</span>
                                <el-select clearable filterable  v-model="type1" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in types" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">读取状态:</span>
                                <el-select clearable filterable  v-model="readStatus1" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in readStatuses" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                                <el-select clearable filterable  v-model="origin1" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">负责人:</span>
                                <el-select clearable filterable  v-model="chargeMan1" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select1">查询</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <router-link :to="{path:'demandDetail', query:{id:scope.row.id,status:scope.row.status}}" style="color:#20a0ff;">{{scope.row.title}}</router-link>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.feedbackTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="期待上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.releaseTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="采纳时间" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.agreedTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="负责人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.chargeMan}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="启动开发时间" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.startTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="任务数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.taskNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="参与开发人数" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.workerNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="已进行时间(周)" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.workedWeeks}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="计划上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.expectOnlineTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="操作" align="center" width="165" fixed="right">
                            <template scope="scope">
                                <el-button @click="feedbackPlan(scope.row)" type="text" size="small" v-show="permit || scope.row.taskNum!=0">计划</el-button>
                                <el-button @click="linkTask(scope.row.id)" type="text" size="small" v-show="permit && scope.row.planId!=null">关联任务</el-button>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange1"
                                :current-page.sync="pageNum1"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="排队中" name="queue" >
                    <div class="demand-top clearfix">
                        <div class="clearfix">
                            <div class="demand-top-list fl">
                                <span class="ttl-name">优先级:</span>
                                <el-select clearable filterable  v-model="priority4" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in prioritys" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">类型:</span>
                                <el-select clearable filterable  v-model="type4" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in types" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">读取状态:</span>
                                <el-select clearable filterable  v-model="readStatus4" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in readStatuses" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                                <el-select clearable filterable  v-model="origin4" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select4">查询</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <router-link :to="{path:'demandDetail', query:{id:scope.row.id,status:scope.row.status}}" style="color:#20a0ff;">{{scope.row.title}}</router-link>
                            </template>
                        </el-table-column>
                        <el-table-column  label="类型" align="center" width="100">
                            <template scope="scope">
                                <span v-if="scope.row.type == 0">个人建议</span>
                                <span v-else-if="scope.row.type == 1">市场反馈</span>
                                <span v-else-if="scope.row.type == 2">公司决策</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="优先级" align="center" width="100">
                            <template scope="scope">
                                <div type="text" v-for="item in prioritys" v-if="item.id == scope.row.priority">{{item.name}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.feedbackTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="期待上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.releaseTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="采纳日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.agreedTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="点赞数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.likesNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="操作" align="center" width="130" fixed="right">
                            <template scope="scope">
                                <el-button @click="feedbackPlan(scope.row)" type="text" size="small" v-show="permit || scope.row.taskNum!=0">计划</el-button>
                                <a style="color:#20a0ff;cursor: pointer;" v-show="permit" @click="deleteDemand(scope.row.id,scope.row.status)">删除</a>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange4"
                                :current-page.sync="pageNum4"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane >
                <el-tab-pane label="不采纳" name="reject" >
                    <div class="demand-top clearfix">
                        <div class="clearfix">
                            <div class="demand-top-list fl">
                                <span class="ttl-name">优先级:</span>
                                <el-select clearable filterable  v-model="priority3" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in prioritys" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">类型:</span>
                                <el-select clearable filterable  v-model="type3" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in types" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name">读取状态:</span>
                                <el-select clearable filterable  v-model="readStatus3" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in readStatuses" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                                <el-select clearable filterable  v-model="origin3" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select3">查询</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <router-link :to="{path:'demandDetail', query:{id:scope.row.id,status:scope.row.status}}" style="color:#20a0ff;">{{scope.row.title}}</router-link>
                            </template>
                        </el-table-column>
                        <el-table-column  label="类型" align="center" width="100">
                            <template scope="scope">
                                <span v-if="scope.row.type == 0">个人建议</span>
                                <span v-else-if="scope.row.type == 1">市场反馈</span>
                                <span v-else-if="scope.row.type == 2">公司决策</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="优先级" align="center" width="100">
                            <template scope="scope">
                                <div type="text" v-for="item in prioritys" v-if="item.id == scope.row.priority">{{item.name}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.feedbackTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="期待上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.releaseTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="点赞数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.likesNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="驳回人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.rejectUser}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="驳回日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.rejectedTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange3"
                                :current-page.sync="pageNum3"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="已完成" name="completed">
                    <div class="demand-top clearfix">
                        <el-date-picker
                                v-model="beginTime2"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                clearable
                        >
                        </el-date-picker>
                        <span style="font-size: 14px;color: #606266;">-</span>
                        <el-date-picker
                                v-model="endTime2"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                clearable
                        >
                        </el-date-picker>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                            <el-select clearable filterable  v-model="origin2" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in originList" :key="item" :label="item"
                                           :value="item"></el-option>
                            </el-select>
                        </div>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">负责人:</span>
                            <el-select clearable filterable  v-model="chargeMan2" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <el-button type="primary" size="small" @click="select2">查询</el-button>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <router-link :to="{path:'demandDetail', query:{id:scope.row.id,status:scope.row.status}}" style="color:#20a0ff;">{{scope.row.title}}</router-link>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.createTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="期待上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.releaseTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="采纳日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.agreedTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="启动开发时间" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.startTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="任务数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.taskNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="参与开发人数" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.workerNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="已进行时间(周)" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.workedTime}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.onlineTime | formatDate}}</span>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange2"
                                :current-page.sync="pageNum2"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="我参与" name="joined">
                    <div class="demand-top clearfix">
                        <el-date-picker
                                v-model="beginTime5"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                clearable
                        >
                        </el-date-picker>
                        <span style="font-size: 14px;color: #606266;">-</span>
                        <el-date-picker
                                v-model="endTime5"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                clearable
                        >
                        </el-date-picker>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                            <el-select clearable filterable  v-model="origin5" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in originList" :key="item" :label="item"
                                           :value="item"></el-option>
                            </el-select>
                        </div>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">负责人:</span>
                            <el-select clearable filterable  v-model="chargeMan5" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <el-button type="primary" size="small" @click="select5">查询</el-button>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center">
                            <template scope="scope">
                                <router-link :to="{path:'demandDetail', query:{id:scope.row.id,status:scope.row.status}}" style="color:#20a0ff;">{{scope.row.title}}</router-link>
                            </template>
                        </el-table-column>
                        <el-table-column  label="当前队列" align="center" >
                            <template scope="scope">
                                <span v-if="scope.row.status == 0">新需求</span>
                                <span v-else-if="scope.row.status == 1">进行中</span>
                                <span v-else-if="scope.row.status == 2">已完成</span>
                                <span v-else-if="scope.row.status == 3">不采纳</span>
                                <span v-else-if="scope.row.status == 4">排队中</span>
                                <span v-else>无状态</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="点赞数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.likesNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="回复数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.replyNum}}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange5"
                                :current-page.sync="pageNum5"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog :visible.sync="newDemandVisible" title="提需求" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" @close="closeDialog('demandForm')">
            <el-form :model="demandForm" ref="demandForm" :rules="rules" label-width="110px" >
                <el-form-item label="需求标题" prop="title">
                    <el-input type="text" v-model="demandForm.title"></el-input>
                </el-form-item>
                <el-form-item label="类型">
                    <el-select v-model="demandForm.type" placeholder="请选择类型">
                        <el-option
                                v-for="item in types"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <br>
                <el-form-item label="优先级">
                    <el-select v-model="demandForm.priority" placeholder="请选择优先级">
                        <el-option
                                v-for="item in prioritys"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <br>
                <el-form-item label="来源" prop="origin">
                    <el-input type="text" v-model="demandForm.origin" clearable></el-input>
                </el-form-item>
                <el-form-item label="问题:" prop="question">
                    <el-input type="textarea" v-model="demandForm.question" placeholder="请简单描述提出该需求要解决什么问题" clearable
                              resize="horizontal" size="large"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="目标:" prop="target">
                    <el-input type="textarea" v-model="demandForm.target" placeholder="请简单描述怎么做，你认为能解决问题" clearable
                              resize="horizontal"></el-input>
                </el-form-item>
                <br>
                <!--<span class="star">*</span>-->
                <el-form-item label="提出日期:" prop="feedbackTime">
                    <el-date-picker
                            v-model="demandForm.feedbackTime"
                            type="date"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择提出日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="期待上线日期:" prop="releaseTime">
                    <el-date-picker
                            v-model="demandForm.releaseTime"
                            type="date"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择期待上线日期">
                    </el-date-picker>
                </el-form-item>
                <br>
                <el-form-item label="上传附件">
                    <el-upload
                            class="avatar-uploader"
                            action=""
                            :http-request="upload"
                            :on-remove="handleRemove1"
                            :before-upload="beforeAvatarUpload"
                    >
                        <i class="el-icon-plus"></i>
                    </el-upload>
                </el-form-item>
                <div class="el-dialog__footer" style=" margin-top: 30px;margin-right: 20px; text-align: center">
                    <el-button @click="saveDemand('demandForm')"  type="primary" :loading="isSaving">保存</el-button>
                </div>
            </el-form>
        </el-dialog>
        <el-dialog :visible.sync="editDemandVisible" title="编辑需求" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" @close="closeDialog('demandForm')">
            <el-form :model="demandForm" ref="demandForm" :rules="rules" label-width="100px">
                <el-form-item label="需求标题" prop="title">
                    <el-input type="text" v-model="demandForm.title"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="类型">
                    <el-select  v-model.number="demandForm.type" placeholder="请选择类型">
                        <el-option
                                v-for="item in types"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <br>
                <el-form-item label="优先级">
                    <el-select v-model="demandForm.priority" placeholder="请选择优先级">
                        <el-option
                                v-for="item in prioritys"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <br>
                <el-form-item label="来源" prop="origin">
                    <el-input type="text" v-model="demandForm.origin" clearable></el-input>
                </el-form-item>
                <el-form-item label="问题:" prop="question">
                    <el-input type="textarea" v-model="demandForm.question" placeholder="请简单描述提出该需求要解决什么问题" clearable
                              resize="horizontal" size="large"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="目标:" prop="target">
                    <el-input type="textarea" v-model="demandForm.target" placeholder="请简单描述怎么做，你认为能解决问题" clearable
                              resize="horizontal"></el-input>
                </el-form-item>
                <br>
                <!--<el-form-item label="期待上线日期:" prop="releaseTime">-->
                    <!--<el-date-picker-->
                            <!--v-model="demandForm.releaseTime"-->
                            <!--type="date"-->
                            <!--placeholder="选择日期时间"-->
                            <!--format="yyyy-MM-dd"-->
                            <!--value-format="yyyy-MM-dd HH:mm:ss"-->
                    <!--&gt;-->
                    <!--</el-date-picker>-->
                <!--</el-form-item>-->
                <div class="el-dialog__footer" style=" margin-top: 30px;margin-right: 20px;">
                    <el-button @click="saveEdit('demandForm')" type="primary" :loading="isSaving">保存</el-button>
                </div>
            </el-form>
        </el-dialog>
        <el-dialog :visible.sync="planVisible" custom-class="myDialog"  title="计划" size="tiny"
                   :close-on-click-modal="false" :close-on-press-escape="false">
            <div class="ctpc-list clearfix" v-if="projectVisible">
                <div class="ctpc-list-menu fl"><span class="star">*</span>项目：</div>
                <div class="demand-top-list fl">
                    <el-select clearable filterable  v-model="project" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                        <el-option v-for="item in projectList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </div>
                <el-button type="primary" size="small" @click="updateDemandProject">添加项目</el-button>
            </div>
            <div class="ctpc-list clearfix" v-else>
                <div class="ctpc-list-menu fl"><span class="star">*</span>项目：</div>
                <div class="ctpc-list-con fl" v-for="item in projectList" v-if="item.id == demandForm.projectId">{{item.name}}</div>
            </div>
            <div class="ctpc-list clearfix">
                <div class="ctpc-list-menu fl"><span class="star">*</span>优先级</div>
                <div class="ctpc-list-con fl" v-for="item in prioritys" v-if="item.id == demandForm.priority">{{item.name}}
                </div>
            </div>
            <div class="ctpc-list clearfix">
                <div class="ctpc-list-menu fl"><span class="star">*</span>预计开始时间:</div>
                <div class="ctpc-list-con fl">
                    <div v-if="!permit || demandForm.status==2" >{{feedbackPlanForm.expectStartTime|formatDate}}</div>
                    <el-date-picker v-model="feedbackPlanForm.expectStartTime" type="date" format="yyyy-MM-dd" v-else=" "
                                    placeholder="选择日期"></el-date-picker>
                </div>
            </div>
            <div class="ctpc-list clearfix">
                <div class="ctpc-list-menu fl"><span class="star">*</span>预计上线时间:</div>
                <div class="ctpc-list-con fl">
                    <div v-if="!permit || demandForm.status==2" >{{feedbackPlanForm.expectOfficialTime|formatDate}}</div>
                    <el-date-picker v-model="feedbackPlanForm.expectOfficialTime" type="date" format="yyyy-MM-dd" v-else=""
                                    placeholder="选择日期"></el-date-picker>
                </div>
            </div>
            <div class="ctpc-member-con">
                <div class="ctpc-member-list clearfix" v-for="(item,index) in planTask"
                     :class="[item.status>1?'done':'in',item.cssClass]">
                    <span class="fl ctpc-member-head" >{{item.userName}}</span>
                    <a class="fl ctpc-member-job-time" @click="directPlanTask(item.id)" style="color:black; cursor: pointer;">{{item.taskName|StringExtract}}</a>
                    <span class="fl ctpc-member-end-time">截止:{{item.endTime|formatDate}}</span>
                    <el-button v-show="permit&&item.id" style="margin-left: 20px" type="text" class="fl ctpc-member-end-time" @click="deleteTask(item.id,feedbackPlanForm.feedbackId,0,demandForm.status)">删除关联</el-button>
                </div>
                <div class="bdl-line"></div>
            </div>
            <div class="ctpc-add-member-detail" v-if="showTaskDetail">
                <div class="add-member-basic">
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>任务名称：</div>
                        <div class="add-member-basic-msg">
                            <input class="member-time-count" v-model="taskStep.taskName" @change=""  style="width:300px">
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>任务描述：
                        </div>
                        <div class="add-member-basic-msg ">
                            <el-input type="textarea" placeholder="添加任务描述" v-model="taskStep.description"
                                      :rows="2" style="width: 300px"></el-input>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>负责人：
                        </div>
                        <div class="add-member-basic-msg fl" >
                            <el-select v-model="taskStep.createBy" clearable placeholder="请选择">
                                <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>截止日期：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <el-date-picker v-model="taskStep.endTime" type="date" format="yyyy-MM-dd"
                                            placeholder="选择日期"></el-date-picker>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>难易度：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <el-select v-model="taskStep.facility" clearable placeholder="请选择">
                                <el-option
                                        v-for="item in facilityList"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>阶段：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <el-select v-model="taskStep.stageId" :multiple-limit="1"
                                       placeholder="请选择">
                                <el-option v-for="item in stageList" :key="item.id"
                                           :label="item.name" :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>标签：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <ul class="ctpc-tags">
                                <li class="tag-lis-add">
                                    <div class="tag-add-sel">
                                        <el-select v-model="taskStep.tags"  multiple placeholder="添加标签">
                                            <el-option v-for="item in tagList" :key="item.id" :label="item.name"
                                                       :value="item.id"></el-option>
                                        </el-select>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="ctpc-btns">
                    <input type="button" class="ctpc-cancel" @click="cancelAddTask" value="取消">
                    <input type="button" class="ctpc-save" @click="saveAddTask" value="确定" >
                </div>
            </div>
            <div v-if="!permit ||demandForm.status==2 "></div>
            <div v-else="">
                <div class="add-member-opt" v-show="!showTaskDetail" >
                    <span class="add-member-icon" >+</span>
                    <span class="add-member-msg" style="margin-top: -40px;margin-left: 24px;" @click="addTask">添加任务</span>
                </div>
                <el-button type="text" icon="check" @click="savePlan(demandForm.status)" style="margin-left: 400px;" :loading="isSaving">保存计划</el-button>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="linkTaskVisible" custom-class="myDialog"  title="关联任务" size="tiny"
                   :close-on-click-modal="false" :close-on-press-escape="false">

            <div class="ctpc-list clearfix">
                <div class="ctpc-list-menu fl"><span class="star">*</span>项目：</div>
                <div class="fl">
                    <el-select v-model="linkTaskId" filterable placeholder="请选择任务">
                        <el-option
                                v-for="item in taskData"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </div>

            </div>
            <div slot="footer">
                <el-button type="primary" @click="deleteTask(linkTaskId,feedbackPlanForm.feedbackId,1,1)">保存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import http from '../lib/Http'
    import moment from 'moment';
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElDialog from "../../node_modules/element-ui/packages/dialog/src/component";
    import helper from '../lib/Helper'
    import 'quill/dist/quill.core.css'
    import 'quill/dist/quill.snow.css'
    import 'quill/dist/quill.bubble.css'

    import { quillEditor } from 'vue-quill-editor'
    import ElOption from "../../node_modules/element-ui/packages/select/src/option";

    moment.locale('zh-cn');
    export default {
        components: {
            ElOption,
            ElDialog,
            quillEditor,
            ElButton},
        name: "Demand3",
        data(){
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
                id:'',
                title:'',
                question:'',
                target:'',
                releaseTime:'',
                feedbackTime:'',
                content:'',
                projectId:'',
                status:'',
                pageNum0:1,
                pageNum1:1,
                pageNum2:1,
                pageNum3:1,
                pageNum4:1,
                pageNum5:1,
                activeName:'new',
                prioritys:[
                    {id:0,name:'普通'},
                    {id:1,name:'紧急'},
                    {id:2,name:'非常紧急'}
                ],

                types:[
                    {id:0,name:'个人建议'},
                    {id:1,name:'市场反馈'},
                    {id:2,name:'公司决策'},
                ],
                readStatuses:[
                    {id:0,name:'未读'},
                    {id:1,name:'已读'},
                ],
                originList:[],
                introducerList:[],
                chargeMan:'',
                chargeManList:[],
                demandData:[],
                planTask:[],
                linkTaskId:'',
                taskData:[],
                taskStep:{
                    taskName:'',
                    index: '',
                    taskType:2,
                    name: '',
                    description: '',
                    projectId:'',
                    createBy:'',
                    endTime: '',
                    stageId: '',
                    priority: '',
                    facility:'',
                    tags: []
                },
                facilityList: [
                    {label: '容易', value: 1},
                    {label: '简单', value: 2},
                    {label: '一般', value: 3},
                    {label: '较难', value: 4},
                    {label: '困难', value: 5},
                ],
                stageList:[],
                tagList:[],
                project:'',
                projectList:[],

                type:'',
                priority:'',
                readStatus:'',
                origin:'',
                introducer:'',


                priority1:'',
                type1:'',
                readStatus1:'',
                origin1:'',
                chargeMan1:'',

                priority4:'',
                type4:'',
                readStatus4:'',
                origin4:'',
                introducer4:'',

                priority3:'',
                type3:'',
                readStatus3:'',
                origin3:'',
                introducer3:'',

                origin2:'',
                chargeMan2:'',
                beginTime2:'',
                endTime2:'',

                origin5:'',
                chargeMan5:'',
                beginTime5:'',
                endTime5:'',

                reqDTO:{
                    pageNum:1,
                },
                proReqDTO:{
                    id:'',
                    projectId:''
                },
                demandPage:{
                    pageSize:10,
                    total:0,
                    total1:0,
                    total3:0,
                    total4:0,
                    currentPage:1
                },
                projectVisible:false,
                showTaskDetail:false,
                planVisible:false,
                isSaving:false,
                newDemandVisible:false,
                editDemandVisible:false,
                demandDeleteIcon:false,
                linkTaskVisible:false,
                demandForm:{
                    id:'',
                    title:'',
                    type:0,
                    priority:0,
                    origin:'',
                    question:'',
                    target:'',
                    releaseTime:'',
                    feedbackTime:'',
                    content:'',
                    projectId:'',
                    status:'',
                    urlList:[]
                },
                demandForm2:{
                    id:'',
                    title:'',
                    type:'',
                    priority:'',
                    origin:'',
                    question:'',
                    target:'',
                    releaseTime:'',
                    feedbackTime:'',
                    content:'',
                    projectId:'',
                    status:'',
                    urlList:[]
                },
                feedbackPlanForm:{
                    id:'',
                    projectId:'',
                    feedbackId:'',
                    expectStartTime:'',
                    expectOfficialTime:''
                },
                rules: {
                    origin: [
                        {required: true,  message: '需求来源不能为空', trigger: 'blur'},
                        {min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur'}
                    ],
                    title: [
                        {required: true,  message: '需求名称不能为空', trigger: 'blur'},
                        {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '类型不能空', trigger:'blur'},
                        {type:'number'}
                    ],
                    priority:[
                        {required: true, message: '优先级不能空', trigger:'blur'}
                    ],
                    question:[
                        {required: true, message: '问题不能为空', trigger:'blur'},
                        {min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur'}
                    ],
                    target:[
                        {required: true, message: '目标不能为空', trigger:'blur'},
                        {min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur'}
                    ],
                    releaseTime:[
                        {type:'date',required: true, message: '期待上线时间不能为空', trigger:'blur'}
                    ],
                    feedbackTime:[
                        {type:'date',required: true, message: '需求提出时间不能为空', trigger:'blur'}
                    ]
                },


            }
        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD');
            },
            StringExtract:function (name) {
                if(name.length>18){
                    return name.substring(0,18)+'...';
                }
                return name;
            },
            dateFilter:function (input) {
                var d = new Date(input);
                var year = d.getFullYear();
                var month = d.getMonth() + 1;
                var day = d.getDate() <10 ? '0' + d.getDate() : '' + d.getDate();
                var hour = d.getHours();
                var minutes = d.getMinutes();
                var seconds = d.getSeconds();
                return  year+ '-' + month + '-' + day;
            }

        },
        beforeMount:function () {
            this.fetchIntroducerList()
            this.fetchOriginList()
            this.fetchNewDemandList()
            this.fetchUserList()
            this.fetchProjectList()
            this.fetchStageList()
            this.fetchTagList()
            this.$root.eventBus.$emit("handleTabSelected", "demand");

        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            getUserName(){
                let userName = helper.decodeToken().userName;
                return userName;
            },

        },
        methods:{

            handleRemove1(file) {
                this.demandForm.urlList.splice(this.demandForm.urlList.findIndex(item=>item.indexOf(file.name)>-1),1)
            },

            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg'||file.type === 'image/png'||file.type === 'image/jpg';
                const isWord = file.type === 'application/msword' || file.type === 'application/pdf' || file.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
                const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                if ((!isJPG) && (!isWord) && (!isExcel) ) {
                    this.$message.error('上传文件只能是 JPG、JPEG、PNG、word、pdf、excel 格式!');
                    return false
                }

                const isLt1M = file.size / 1024 / 1024 < 1;
                if (!isLt1M) {
                    this.$message.error('上传文件大小不能超过 1MB!');
                }
                return  isLt1M;
            },

            upload(file) {
                var data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp('/upload/file',data,(res)=> {
                    this.demandForm.urlList.push(res.data.url)
                })
            },
            //添加项目
            updateDemandProject(){
                if (this.project){
                    this.proReqDTO.id = String(this.feedbackPlanForm.feedbackId)
                    this.proReqDTO.projectId = String(this.project)
                }
                http.zsyPostHttp('/feedback/demand/project/add', this.proReqDTO, (res) => {
                    this.$message({
                        showClose: true,
                        message: '项目添加成功',
                        type: 'success'
                    });
                    this.planVisible = false
                    this.projectVisible = false
                    this.fetchQueueDemandList()
                })
            },


            //查询项目
            fetchProjectList() {
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    this.projectList = resp.data
                })
            },

            //查询来源
            fetchOriginList() {
                http.zsyGetHttp('/feedback/originList', {}, (resp) => {
                    this.originList = resp.data
                })
            },

            //查询提出人
            fetchIntroducerList(){
                http.zsyGetHttp('/feedback/demand/introducers', {}, (res) => {
                    this.introducerList = res.data
                })
            },

            //查询负责人
            fetchUserList() {
                let vm = this
                http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.chargeManList = resp.data
                })
            },

            //查询新需求列表
            fetchNewDemandList(){
                this.reqDTO.pageNum = this.pageNum0
                http.zsyPostHttp('/feedback/demand/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.totalSize
                })
            },

            //查询进行中需求
            fetchRunningDemandList(){
                this.reqDTO.pageNum = this.pageNum1
                http.zsyPostHttp('/feedback/demand-running/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.totalSize
                })
            },

            //查询排队中需求
            fetchQueueDemandList(){
                this.reqDTO.pageNum = this.pageNum4
                http.zsyPostHttp('/feedback/demand-queued/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.totalSize
                })
            },

            //查询不采纳需求
            fetchRejectedDemandList(){
                this.reqDTO.pageNum = this.pageNum3
                http.zsyPostHttp('/feedback/demand-rejected/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.totalSize
                })
            },

            //查询已完成需求
            fetchCompletedDemandList(){
                this.reqDTO.pageNum = this.pageNum2
                http.zsyPostHttp('/feedback/demand-completed/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.total
                })
            },

            //查询我参与的需求
            fetchJoinedDemandList(){
                this.reqDTO.pageNum = this.pageNum5
                http.zsyPostHttp('/feedback/demand-joined/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.total
                })
            },

            //取消添加任务
            cancelAddTask(){
                this.showTaskDetail = !this.showTaskDetail;
                if (this.taskStep.index !== '') {
                    this.planTask[this.taskStep.index] = this.stepTemp;
                    // 取消css
                    this.planTask[this.taskStep.index].cssClass = ''
                }
                this.taskStep = {
                    index: '',
                    taskName:'',
                    createBy:'',
                    taskType:2,
                    description: '',
                    userName:'',
                    endTime: '',
                    projectId: '',
                    priority: '',
                    facility: '',
                    stageId:'',
                    tags:''
                }
            },

            warnMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'warning'
                });
            },

            //添加任务
            saveAddTask() {
                if (this.taskStep.taskName == '') {
                    this.warnMsg("请填写任务名称");
                    return;
                }
                if (this.taskStep.taskName.length >100  ) {
                    this.warnMsg("任务名称请勿超过100字");
                    return;
                }
                if (this.taskStep.createBy == '') {
                    this.warnMsg("请填写任务负责人");
                    return;
                }
                if (this.taskStep.endTime == '') {
                    this.warnMsg("请选择结束时间");
                    return;
                }
                if (this.taskStep.stageId === '') {
                    this.warnMsg("请选择项目阶段");
                    return;
                }
                if (this.taskStep.facility === '') {
                    this.warnMsg("请选择任务难易度");
                    return;
                }
                if (this.taskStep.tags.length == 0) {
                    this.warnMsg("请选择至少一项标签");
                    return;
                }
                for(var i =0;i< this.chargeManList.length;i++){
                    if(this.taskStep.createBy == this.chargeManList[i].id){
                        this.taskStep.userName = this.chargeManList[i].name
                    }
                }

                this.showTaskDetail = !this.showTaskDetail;
                if (this.taskStep.index === '') {
                    let task = {}
                    task.taskName = this.taskStep.taskName.trim()
                    task.description = this.taskStep.description.trim()
                    task.createBy = this.taskStep.createBy
                    task.endTime = moment(this.taskStep.endTime).format('YYYY-MM-DD 23:59:59')
                    task.taskType = this.taskStep.taskType
                    task.projectId = 0
                    task.priority = this.demandForm.priority
                    task.facility = this.taskStep.facility
                    task.stageId = this.taskStep.stageId
                    task.tags = this.taskStep.tags
                    task.userName = this.taskStep.userName
                    this.planTask.push(task)
                } else {
                    // 取消css
                    this.planTask[this.taskStep.index].cssClass = ''
                }

                this.taskStep = {
                    index: '',
                    taskName:'',
                    createBy:'',
                    taskType:2,
                    description: '',
                    userName:'',
                    endTime: '',
                    projectId: '',
                    priority: '',
                    facility: '',
                    stageId:'',
                    tags:''
                }
                this.stepTemp = {}
            },

            //保存计划
            savePlan(num){
                this.isSaving = true
                if (this.feedbackPlanForm.expectStartTime == '') {
                    this.warnMsg("请选择预计开始时间");
                    this.isSaving = false
                    return;
                }
                if (this.feedbackPlanForm.expectOfficialTime == '') {
                    this.warnMsg("请填写预计上线时间");
                    this.isSaving = false
                    return;
                }
                let param = this.feedbackPlanForm;
                param.expectOfficialTime = moment(param.expectOfficialTime).format('YYYY-MM-DD 23:59:59');
                param.expectStartTime = moment(param.expectStartTime).format('YYYY-MM-DD 00:00:00');
                param['planTask'] = this.planTask;
                http.zsyPostHttp('/feedback/addPlan', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: '计划创建成功',
                        type: 'success'
                    });
                    //刷新  进行中需求列表
                    if (num == 1){
                        this.fetchRunningDemandList();
                    }
                    //刷新   排队中需求列表
                    if (num == 4){
                        this.fetchQueueDemandList()
                    }
                    this.planVisible = false;
                    this.isSaving = false
                })
            },

            //删除关联任务
            deleteTask(taskId,feedbackId,link,num){
                //任务关联
                http.zsyPutHttp('/feedback/task/'+feedbackId+'/'+taskId, null,(resp) => {
                    if(link==0){
                        this.$message({ showClose: true,message: '删除关联任务成功',type: 'success'});
                    }else{
                        this.$message({ showClose: true,message: '任务关联成功',type: 'success'});
                    }

                    //刷新  进行中需求列表
                    if (num == 1){
                        this.fetchRunningDemandList();
                    }
                    //刷新   排队中需求列表
                    if (num == 4){
                        this.fetchQueueDemandList()
                    }

                    this.linkTaskVisible = false;
                    this.planVisible = false
                    this.isSaving = false
                    this.linkTaskId = ''
                })
            },

            //跳转到任务页面
            directPlanTask(taskId){
                if(taskId!=null&&taskId!=''){
                    this.$router.push({name:'taskListFormComments', params:{ taskId:taskId }})
                }
            },

            //需求读取
            read(id){
                http.zsyPostHttp('/feedback/demand/read/'+String(id),{}, (resp) => {

                })
            },

            //关联任务
            linkTask(id){
                this.linkTaskVisible = true
                http.zsyGetHttp('/task/plan', {}, (resp) => {
                    this.taskData = resp.data
                })
                this.feedbackPlanForm.feedbackId=id
            },

            //查询需求计划
            feedbackPlan(feedback){
                this.clearPlan();
                http.zsyGetHttp('/feedback/demand/project/'+feedback.id,{},(res) => {
                    if (res.data.projectId == 0 || res.data.projectId == null){
                        //此时没有添加项目
                        this.projectVisible = true
                    }
                })
                this.feedbackPlanForm.feedbackId = feedback.id
                this.feedbackPlanForm.projectId = feedback.projectId
                this.demandForm.projectId =feedback.projectId
                this.demandForm.priority =feedback.priority
                this.demandForm.status = feedback.status
                this.planVisible=true

                http.zsyGetHttp('/feedback/getPlan/'+feedback.id, {}, (resp) => {
                    if(resp.data.planTask&&resp.data.planTask.length!=0){
                        this.feedbackPlanForm.id = resp.data.id
                        this.feedbackPlanForm.expectOfficialTime =resp.data.expectOfficialTime
                        this.feedbackPlanForm.expectStartTime =resp.data.expectStartTime
                        this.planTask = resp.data.planTask
                    }
                    this.feedbackPlanForm.id = resp.data.id
                    this.feedbackPlanForm.expectOfficialTime =resp.data.expectOfficialTime
                    this.feedbackPlanForm.expectStartTime =resp.data.expectStartTime
                })
            },

            //查询阶段
            fetchStageList() {
                let vm = this
                http.zsyGetHttp('/stage/list', {}, (resp) => {
                    vm.stageList = resp.data
                })
            },

            //清除计划
            clearPlan(){
                this.feedbackPlanForm.feedbackId=''
                this.feedbackPlanForm.id=''
                this.feedbackPlanForm.expectOfficialTime = ''
                this.feedbackPlanForm.expectStartTime = ''
                this.planTask = []
            },

            //查询标签
            fetchTagList() {
                let vm = this
                http.zsyGetHttp('/tag/list', {}, (resp) => {
                    vm.tagList = resp.data
                })
            },

            clearReqDTO(){

                this.reqDTO.priority = null;
                this.reqDTO.readStatus = null;
                this.reqDTO.user = null;
                this.reqDTO.chargeMan = null;
                this.reqDTO.origin = null;
                this.reqDTO.type = null;
                this.reqDTO.beginTime = null;
                this.reqDTO.endTime = null;
            },
            clear0(){
                this.priority = null
                this.type = null
                this.readStatus = null
                this.origin = null
                this.introducer = null
            },
            clear1(){
              this.priority1 = null
              this.type1 = null
              this.readStatus1 = null
              this.origin1 = null
              this.chargeMan1 = null
            },
            clear4(){
                this.priority4 = null
                this.type4 = null
                this.readStatus4 = null
                this.origin4 = null
                this.introducer4 = null
            },
            clear3(){
                this.priority3 = null
                this.type3 = null
                this.readStatus3 = null
                this.origin3 = null
                this.introducer3 = null
            },
            clear2(){
                this.introducer2 = null
                this.chargeMan2 = null
                this.beginTime2 = null
                this.endTime2 = null
            },
            clear5(){
                this.introducer5 = null
                this.chargeMan5 = null
                this.beginTime5 = null
                this.endTime5 = null
            },

            //条件查询新需求
            select0(){
                this.clearReqDTO()
                this.reqDTO.pageNum = 1

                if (this.priority != undefined && this.priority != null){
                    this.reqDTO.priority = this.priority
                }
                if(this.readStatus != undefined && this.priority != null){
                    this.reqDTO.readStatus = this.readStatus
                }
                if(this.origin){
                    this.reqDTO.origin = this.origin
                }
                if (this.type != undefined && this.priority != null){
                    this.reqDTO.type = this.type
                }

                this.fetchNewDemandList()
            },
            //条件查询进行中需求
            select1(){
                this.clearReqDTO()
                this.reqDTO.pageNum = 1

                if (this.priority1 != undefined && this.priority1 != null){
                    this.reqDTO.priority = this.priority1
                }
                if(this.readStatus1 != undefined && this.readStatus1 != null){
                    this.reqDTO.readStatus = this.readStatus1
                }
                if (this.type1 != undefined && this.type1 != null){
                    this.reqDTO.type = this.type1
                }
                if (this.chargeMan1){
                    this.reqDTO.chargeMan = String(this.chargeMan1)
                }
                if(this.origin1){
                    this.reqDTO.origin = this.origin1
                }

                this.fetchRunningDemandList()
            },

            //条件查询排队中需求
            select4(){
                this.clearReqDTO()
                this.reqDTO.pageNum = 1

                if (this.priority4 != undefined && this.priority4 != null){
                    this.reqDTO.priority = this.priority4
                }
                if(this.readStatus4 != undefined && this.readStatus4 != null){
                    this.reqDTO.readStatus = this.readStatus4
                }
                if(this.origin4){
                    this.reqDTO.origin = this.origin4
                }
                if (this.type4 != undefined && this.type4 != null){
                    this.reqDTO.type = this.type4
                }

                this.fetchQueueDemandList()
            },

            //条件查询驳回需求
            select3(){
                this.clearReqDTO()
                this.reqDTO.pageNum = 1

                if (this.priority3 != undefined && this.priority3 != null){
                    this.reqDTO.priority = this.priority3
                }
                if(this.readStatus3 != undefined && this.readStatus3 != null){
                    this.reqDTO.readStatus = this.readStatus3
                }
                if(this.origin3){
                    this.reqDTO.origin = this.origin3
                }
                if (this.type3 != undefined && this.type3 != null){
                    this.reqDTO.type = this.type3
                }

                this.fetchRejectedDemandList()
            },

            //条件查询已完成需求
            select2(){
                this.clearReqDTO()
                this.reqDTO.pageNum = 1


                if(this.origin2){
                    this.reqDTO.origin = this.origin2
                }
                if (this.beginTime2){
                    this.reqDTO.beginTime = moment(this.beginTime2).format('YYYY-MM-DD 00:00:00')
                }
                if (this.endTime2){
                    this.reqDTO.endTime = moment(this.endTime2).format('YYYY-MM-DD 23:59:59')
                }
                if (this.chargeMan2){
                    this.reqDTO.chargeMan = String(this.chargeMan2)
                }

                this.fetchCompletedDemandList()
            },

            //查询我参与的需求
            select5(){
                this.clearReqDTO()
                this.reqDTO.pageNum = 1


                if(this.origin5){
                    this.reqDTO.origin = this.origin5
                }
                if (this.beginTime5){
                    this.reqDTO.beginTime = moment(this.beginTime5).format('YYYY-MM-DD 00:00:00')
                }
                if (this.endTime5){
                    this.reqDTO.endTime = moment(this.endTime5).format('YYYY-MM-DD 23:59:59')
                }
                if (this.chargeMan5){
                    this.reqDTO.chargeMan = String(this.chargeMan5)
                }

                this.fetchJoinedDemandList()
            },

            //添加任务
            addTask(){
                this.showTaskDetail = true
            },

            //跳转到需求详情页
            demandDetail(id){
                this.read(id);
                this.$router.push({
                    path:'demandDetail',
                    name:'demandDetail',
                    params:{
                        demandId:String(id),
                        flush:true
                    }
                })
            },

            //关闭dialog
            closeDialog(formName){
              this.$refs[formName].resetFields();
              this.isSaving = false
            },

            //添加需求
            saveDemand(formName){
                this.isSaving = true
                this.$refs[formName].validate((valid) => {
                    if (valid){
                        if (this.demandForm.type == null){
                            this.$message({ showClose: true,message: '类型不能为空',type: 'warning'});
                        }
                        if(this.demandForm.priority == null){
                            this.$message({ showClose: true,message: '优先级不能为空',type: 'warning'});
                        }
                        var param = this.demandForm
                        // param.type = Number(param.type)
                        param.releaseTime = moment(param.releaseTime).format('YYYY-MM-DD HH:00:00')
                        param.feedbackTime = moment(param.feedbackTime).format('YYYY-MM-DD HH:00:00')

                            http.zsyPostHttp('/feedback/demand/add', param, (resp) => {
                                this.$message({ showClose: true,message: '需求创建成功',type: 'success'});
                                this.$refs[formName].resetFields();
                                this.newDemandVisible = false
                                this.fetchNewDemandList();
                                this.isSaving =false
                            });
                    }
                })

            },

            saveEdit(formName){
                this.isSaving = true
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var param = this.demandForm
                        if (this.demandForm.type == null){
                            this.$message({ showClose: true,message: '类型不能为空',type: 'warning'});
                        }
                        if(this.demandForm.priority == null){
                            this.$message({ showClose: true,message: '优先级不能为空',type: 'warning'});
                        }
                        param.releaseTime = moment(param.releaseTime).format('YYYY-MM-DD HH:00:00')
                        http.zsyPutHttp('/feedback/demand/edit/'+this.demandForm.id, param, (resp) => {
                            this.$message({ showClose: true,message: '需求修改成功',type: 'success'});
                            this.$refs[formName].resetFields();
                            this.editDemandVisible = false
                            if (this.demandForm.status == 0){
                                this.fetchNewDemandList()
                            }
                            this.isSaving =false
                    })
                    }
                })
            },

            //编辑需求
            editDemand(demand){
                if (this.permit){
                    this.clearDemandForm()
                    this.editDemandVisible = true
                    this.demandDeleteIcon = true
                    this.demandForm.id = demand.id
                    this.demandForm.title = demand.title
                    this.demandForm.type = demand.type
                    this.demandForm.priority = demand.priority
                    this.demandForm.origin = demand.origin
                    this.demandForm.question = demand.question
                    this.demandForm.target = demand.target
                    this.demandForm.releaseTime = demand.releaseTime
                    this.demandForm = Object.assign({},this.demandForm)
                }else {
                    this.demandDetail(demand.id)
                    this.clearDemandForm()
                }
            },

            //删除需求
            deleteDemand(id,status){
                this.$confirm('此操作将删除该需求, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp('/feedback/delete/'+id, null, (resp) => {
                        this.$message({ showClose: true,message: '需求删除成功',type: 'success'});
                        if (status == 0){
                            this.fetchNewDemandList()
                        }else if (status == 4){
                            this.fetchQueueDemandList()

                        }
                    });
                }).catch(() => {
                });
            },

            //清空需求表单
            clearDemandForm(){
                this.demandForm.id = null
                this.demandForm.title = null
                this.demandForm.type = 0
                this.demandForm.priority= 0
                this.demandForm.origin= null
                this.demandForm.question = null
                this.demandForm.target = null
                this.demandForm.releaseTime = null
                this.demandForm.feedbackTime = null
                this.demandForm.urlList = []
            },

            handleCurrentChange(currentPage){
                this.fetchNewDemandList()

            },
            handleCurrentChange1(currentPage){
                this.fetchRunningDemandList()

            },
            handleCurrentChange2(currentPage){
                this.fetchCompletedDemandList()

            },
            handleCurrentChange3(currentPage){
                this.fetchRejectedDemandList()

            },
            handleCurrentChange4(currentPage){
                this.fetchQueueDemandList()

            },
            handleCurrentChange5(currentPage){
                this.fetchJoinedDemandList()

            },

            feedbackPageLayout() {
                if (this.demandPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            handleClick(tab, event) {
                if(tab.name =="new"){
                    this.clear0()
                    this.clearReqDTO()
                    this.fetchNewDemandList()
                }else if(tab.name =="running"){
                    this.clear1()
                    this.clearReqDTO()
                    this.fetchRunningDemandList()
                }else if (tab.name=='queue'){
                    this.clear4()
                    this.clearReqDTO()
                    this.fetchQueueDemandList()
                }else if (tab.name == 'reject'){
                    this.clear3()
                    this.clearReqDTO()
                    this.fetchRejectedDemandList()
                } else  if (tab.name == 'completed'){
                    this.clear2()
                    this.clearReqDTO()
                    this.fetchCompletedDemandList()
                } else if (tab.name == 'joined'){
                    this.clear5()
                    this.clearReqDTO()
                    this.fetchJoinedDemandList()
                }
            },
        },
    }
</script>

<style scoped>

    .demand-con {
        width: 1100px;
        margin: auto;
    }

    .demand-top {
        background: #fff;
        padding-top: 20px;
        border-radius: 4px;
        box-shadow: 0 0 10px #ccc;
        margin-bottom: 24px;
    }

    .demand-top-list {
        margin: 0 20px 5px 20px;
    }


    .tag-name button {
        margin: 0 10px 10px 0;
    }

    .tag-name button:focus {
        color: #545454;
        border-color: #c4c4c4;
    }

    .tag-name button.active {
        color: #36A8FF;
        border-color: #36A8FF;
    }

    .ttl-name {
        margin-right: 50px;
        font-size: 14px;
    }

    .pagination .el-pagination {
        text-align: right;
        padding-right: 0;
        margin-top: 20px;
        margin-right: 10px;
    }
</style>
<style>

    .stepActive {
        box-shadow: 0 0 10px #20A0FF !important;
    }

    .star {
        color: red;
        padding: 1px;
    }

    .ctpc-list-con .el-select {
        width: 188px;
    }

    .ctpc-list-menu {
        line-height: 30px;
        width: 170px;
        font-size: 14px;
    }

    .ctpc-list-con {
        width: 220px;
    }

    .ctpc-list {
        background: #fff;
        padding: 6px 10px; /* border: 1px solid #ccc; */
        margin-bottom: 10px;
    }

    .ctpc-tags {
        position: relative;
    }

    .ctpc-tags > li {
        display: inline-block;
        line-height: 30px;
    }

    .ctpc-tag-lis > span {
        display: inline-block;
        vertical-align: middle;
        font-size: 14px;
    }

    .ctpc-tag-lis .tag-lis-delete:hover {
        text-shadow: 0 0 8px #ccc;
        font-size: 18px !important;
    }

    .tag-lis-add {
        position: relative;
        /*margin-left: 10px;*/
        cursor: pointer;
    }


    .ctpc-member-con {
        margin-left: 6px;
        position: relative;
    }

    .ctpc-member-list {
        height: 42px;
        background: #fff;
        border: 1px solid #ccc;
        line-height: 42px;
        color: #000;
        padding: 0 4px;
        position: relative;
        margin-bottom: 10px;
        box-shadow: 0 0 10px #ccc;
        display: -webkit-flex;
        display: -moz-flex;
        display: -ms-flex;
        display: -o-flex;
        display: flex;
    }

    .ctpc-member-list > span {
        display: inline-block;
        vertical-align: middle;
    }

    .ctpc-member-job-time {
        width: 280px;
    }

    .ctpc-member-end-time {
        /*width: 110px;*/
    }

    .ctpc-member-head {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: #006699;
        color: #fff;
        font-size: 10px;
        text-align: center;
        line-height: 36px;
        margin-top: 3px;
        overflow: hidden;
        margin-right: 10px;
    }

    .add-member-opt {
        cursor: pointer;
        margin: 20px 10px;
        width: 80px;
    }

    .add-member-opt > span {
        display: inline-block;
        vertical-align: middle;
    }

    .add-member-icon {
        background: #36A8FF;
        color: #fff;
        width: 20px;
        height: 20px;
        text-align: center;
        font-size: 20px;
        line-height: 16px;
        font-weight: bold;
        border-radius: 50%;
    }

    .add-member-msg {
        color: #36A8FF;
        margin-left: 6px;
    }

    .ctpc-btns {
        text-align: right;
        margin-top: 20px;
    }

    .ctpc-btns > input {
        display: inline-block;
        width: 80px;
        height: 28px;
        margin-left: 12px;
        cursor: pointer;
        border-radius: 4px;
    }

    .ctpc-cancel {
        background: #fff;
        border: 1px solid #ccc;
    }

    .ctpc-cancel:hover {
        background: #fff;
        border: 1px solid #36A8FF;
        color: #36A8FF;
        font-weight: 700;
    }

    .ctpc-save {
        background: #36A8FF;
        color: #fff;
    }

    .ctpc-con {
        height: 500px;
        overflow-y: scroll;
        padding-right: 10px;
    }

    .ctpc-con::-webkit-scrollbar {
        width: 10px;
        background-color: #fff;
    }

    .ctpc-con::-webkit-scrollbar-thumb {
        background-color: rgb(255, 255, 255);
        background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));
        border-radius: 10px;
    }

    .ctpc-con::-webkit-scrollbar-track {
        /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.1); */
        background-color: #fff;
        border-radius: 10px;
    }

    .ctpc-instruction-msg {
        padding: 4px;
        cursor: pointer;
    }

    .ctpc-instruction-msg:hover {
        color: #3DA8F5;
        background: #F5F5F5;
    }

    .add-member-remark {
        border: 1px solid #ccc;
        display: block;
        padding: 6px;
        width: 512px;
    }

    .add-member-basic {
        background: #fff;
        border: 1px solid #ccc;
        margin-top: 10px;
        padding: 10px;
        font-size: 14px;
    }

    .add-member-basic-menu {
        width: 82px;
        text-align: right;
    }

    .add-member-basic-list { /* border-bottom: 1px solid #ccc; */
        padding: 5px 0;
        line-height: 34px;
    }

    .add-member-basic-list:last-child {
        border: none;
    }

    .add-stage-opt {
        color: #36A8FF;
        cursor: pointer;
        width: 400px;
    }

    .add-member-basic-msg .el-select {
        width: 120px;
    }

    .member-time-count {
        width: 40px;
        border: 1px solid #ccc;
        height: 26px;
        border-radius: 4px;
        margin-right: 4px;
        text-indent: 4px;
    }

    .member-time-week{
        width: 40px;
        border: 1px solid #ccc;
        height: 26px;
        border-radius: 4px;
        text-indent: 4px;
    }

    .add-member-basic-time {
        margin-left: 40px;
    }

    .add-member-basic-msg .el-date-editor.el-input {
        width: 140px;
    }

    .add-member-basic-end {
        margin-left: 40px;
    }

    .tag-add-sel .el-select {
        width: 180px;
    }

    .tag-add-sel {
        /*position: absolute;*/
        left: -10px;
        top: 0;
        z-index: 100;
    }

    .add-tag-btn {
        background: #36A8FF;
        color: #fff;
        position: absolute;
        right: 0;
        top: 50%;
        width: 40px;
        height: 30px;
        z-index: 200;
        text-align: center;
        transform: translateY(-50%);
        border-radius: 4px;
    }

    .ctpc-member-delete {
        font-size: 26px;
        cursor: pointer;
        margin-right: 4px;
    }

    .add-member-stage {
        position: relative;
    }

    .ctpc-ins-edit {
        margin-right: 10px;
    }

    .ctpc-ins-edit .ctpc-btns {
        margin-top: 0;
    }

</style>

<style>
    .quill-editor {
        height: 745px;

    .ql-container {
        height: 680px;
    }
    }

    .limit {
        height: 30px;
        border: 1px solid #ccc;
        line-height: 30px;
        text-align: right;

    span {
        color: #ee2a7b;
    }
    }

    .ql-snow .ql-editor img {
        max-width: 480px;
    }

    .ql-editor .ql-video {
        max-width: 480px;
    }


</style>


<style>
    .el-dialog__wrapper .myDialog {
        width: 600px !important;
    }

    .my-dialog-title {
        font-size: 16px;
        font-weight: bold;
    }

    .my-dialog-title-tool {
        float: right;
    }


</style>
