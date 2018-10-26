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
                                <span class="ttl-name" style="margin-left: -15px;">来源:</span>
                                <el-select clearable filterable  v-model="origin" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                                <el-select clearable filterable  v-model="introducer" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in introducerList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select(0)">查询</el-button>
                            <el-button type="primary" size="small" @click="newDemandVisible=true,clearDemandForm()" v-show="permit">提需求</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column prop="no" label="序号" align="center" width="70" type="index">
                        </el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <!--<a style="color:#20a0ff;cursor: pointer;" @click.stop.prevent="demandDetail(scope.row.id)">{{scope.row.title}}</a>-->
                                <router-link :to="{path:'demandDetail', query:{id:scope.row.id}}" style="color:#20a0ff;">{{scope.row.title}}</router-link>
                            </template>
                        </el-table-column>
                        <el-table-column  label="来源" align="center" width="100">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="类型" align="center" width="100">
                            <template scope="scope">
                                <span v-if="scope.row.type == 0">个人建议</span>
                                <span v-else-if="scope.row.type == 1">市场反馈</span>
                                <span v-else>公司决策</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="优先级" align="center" width="100">
                            <template scope="scope">
                                <div type="text" v-for="item in prioritys" v-if="item.id == scope.row.priority">{{item.name}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.createBy}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.createTime| formatDate}}</span>
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
                        <el-table-column label="操作" align="center">
                            <template scope="scope">
                                <a style="color:#20a0ff;cursor: pointer;" @click="editDemandVisible=true,editDemand(scope.row)">编辑</a>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange"
                                :current-page.sync="demandPage.pageNum"
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
                                <span class="ttl-name" style="margin-left: -15px;">来源:</span>
                                <el-select clearable filterable  v-model="origin" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">负责人:</span>
                                <el-select clearable filterable  v-model="chargeMan" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select(1)">查询</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <a style="color:#20a0ff;cursor: pointer;" @click="demandDetail(scope.row.id)">{{scope.row.title}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.createBy}}</span>
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
                        <el-table-column  label="已进行时间" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.workedWeeks}}周</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="计划上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.expectOnlineTime | formatDate}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="操作" align="center" width="165">
                            <template scope="scope">
                                <el-button @click="feedbackPlan(scope.row)" type="text" size="small" v-show="permit || scope.row.taskNum!=0">计划</el-button>
                                <el-button @click="linkTask(scope.row.id)" type="text" size="small" v-show="permit && scope.row.planId!=null">关联任务</el-button>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange1"
                                :current-page.sync="demandPage.pageNum"
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
                                <span class="ttl-name" style="margin-left: -15px;">来源:</span>
                                <el-select clearable filterable  v-model="origin" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                                <el-select clearable filterable  v-model="introducer" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in introducerList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select(4)">查询</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <a style="color:#20a0ff;cursor: pointer;" @click="demandDetail(scope.row.id)">{{scope.row.title}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column  label="来源" align="center" width="100">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="类型" align="center" width="100">
                            <template scope="scope">
                                <span v-if="scope.row.type == 0">个人建议</span>
                                <span v-else-if="scope.row.type == 1">市场反馈</span>
                                <span v-else>公司决策</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="优先级" align="center" width="100">
                            <template scope="scope">
                                <div type="text" v-for="item in prioritys" v-if="item.id == scope.row.priority">{{item.name}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.createBy}}</span>
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
                        <el-table-column label="点赞数" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.likesNum}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="操作" align="center" width="130">
                            <template scope="scope">
                                <el-button @click="feedbackPlan(scope.row)" type="text" size="small" v-show="permit || scope.row.taskNum!=0">计划</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange4"
                                :current-page.sync="demandPage.pageNum"
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
                                <span class="ttl-name" style="margin-left: -15px;">来源:</span>
                                <el-select clearable filterable  v-model="origin" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in originList" :key="item" :label="item"
                                               :value="item"></el-option>
                                </el-select>
                            </div>
                            <div class="demand-top-list fl">
                                <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                                <el-select clearable filterable  v-model="introducer" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                    <el-option v-for="item in introducerList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <el-button type="primary" size="small" @click="select(3)">查询</el-button>
                        </div>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column type="index" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <a style="color:#20a0ff;cursor: pointer;" @click="demandDetail(scope.row.id)">{{scope.row.title}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column  label="来源" align="center" width="100">
                            <template scope="scope">
                                <span>{{scope.row.origin}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="类型" align="center" width="100">
                            <template scope="scope">
                                <span v-if="scope.row.type == 0">个人建议</span>
                                <span v-else-if="scope.row.type == 1">市场反馈</span>
                                <span v-else>公司决策</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="优先级" align="center" width="100">
                            <template scope="scope">
                                <div type="text" v-for="item in prioritys" v-if="item.id == scope.row.priority">{{item.name}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.createBy}}</span>
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
                                :current-page.sync="demandPage.pageNum"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="已完成" name="completed">
                    <div class="demand-top clearfix">
                        <el-date-picker
                                v-model="beginTime"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="不能为空"
                        >
                        </el-date-picker>
                        <span style="font-size: 14px;color: #606266;">-</span>
                        <el-date-picker
                                v-model="endTime"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="不能为空"
                        >
                        </el-date-picker>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                            <el-select clearable filterable  v-model="introducer" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in introducerList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">负责人:</span>
                            <el-select clearable filterable  v-model="chargeMan" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <el-button type="primary" size="small" @click="select(2)">查询</el-button>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column prop="no" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <a style="color:#20a0ff;cursor: pointer;" @click="demandDetail(scope.row.id)">{{scope.row.title}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出人" align="center" width="80">
                            <template scope="scope">
                                <span>{{scope.row.createBy}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="提出日期" align="center" width="120">
                            <template scope="scope">
                                <span>{{scope.row.createTime}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="期待上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.releaseTime}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="采纳日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.agreedTime}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="启动开发时间" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.startTime}}</span>
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
                        <el-table-column  label="已进行时间" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.workedWeeks}}周</span>
                            </template>
                        </el-table-column>
                        <el-table-column  label="上线日期" align="center" width="130">
                            <template scope="scope">
                                <span>{{scope.row.onlineTime}}</span>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleCurrentChange2"
                                :current-page.sync="demandPage.pageNum"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="我参与" name="joined">
                    <div class="demand-top clearfix">
                        <el-date-picker
                                v-model="beginTime"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                        >
                        </el-date-picker>
                        <span style="font-size: 14px;color: #606266;">-</span>
                        <el-date-picker
                                v-model="endTime"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                        >
                        </el-date-picker>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">提出人:</span>
                            <el-select clearable filterable  v-model="introducer" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in introducerList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="demand-top-list fl">
                            <span class="ttl-name" style="margin-left: -15px;">负责人:</span>
                            <el-select clearable filterable  v-model="chargeMan" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                                <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <el-button type="primary" size="small" @click="select(2)">查询</el-button>
                    </div>
                    <el-table :data="demandData" border>
                        <el-table-column prop="no" label="序号" align="center" width="70"></el-table-column>
                        <el-table-column  label="需求标题" align="center" width="250">
                            <template scope="scope">
                                <a style="color:#20a0ff;cursor: pointer;" @click="demandDetail(scope.row.id)">{{scope.row.title}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column  label="当前队列" align="center" width="100">
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
                                :current-page.sync="demandPage.pageNum"
                                :page-size="demandPage.pageSize"
                                :layout="feedbackPageLayout()"
                                :total="demandPage.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog :visible.sync="newDemandVisible" title="提需求" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <el-form :model="demandForm" ref="demandForm" :rules="rules" label-width="110px" :inline="true">
                <el-form-item label="需求标题">
                    <el-input prop="title" type="text" v-model="demandForm.title" clearable></el-input>
                </el-form-item><br>
                <el-form-item label="类型">
                    <el-select v-model="demandForm.type" placeholder="请选择类型" size="small" style="width:100px;margin-left: -50px">
                        <el-option
                                    v-for="item in types"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="优先级">
                    <el-select v-model="demandForm.priority" placeholder="请选择优先级" size="small" style="width:100px;margin-left: -50px">
                        <el-option
                                v-for="item in prioritys"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="来源">
                    <el-input type="text" v-model="demandForm.origin" clearable></el-input>
                </el-form-item>
                <el-form-item label="问题:">
                    <el-input type="textarea" v-model="demandForm.question" placeholder="请简单描述提出该需求要解决什么问题" clearable resize="horizontal"></el-input>
                </el-form-item><br>
                <el-form-item label="目标:">
                    <el-input type="textarea" v-model="demandForm.target" placeholder="请简单描述怎么做，你认为能解决问题" clearable
                              resize="horizontal" ></el-input>
                </el-form-item><br>
                <el-form-item label="期待上线日期:">
                    <el-date-picker
                            v-model="demandForm.releaseTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <div class="el-dialog__footer" style=" margin-top: 30px;margin-right: 20px;">
                    <el-button type="danger" @click="deleteDemand()"v-show="demandDeleteIcon">删除需求</el-button>
                    <el-button @click="saveDemand('demandForm')" type="primary" :loading="isSaving">保存</el-button>
                </div >
            </el-form>
        </el-dialog>
        <el-dialog :visible.sync="planVisible" custom-class="myDialog"  title="计划" size="tiny"
                   :close-on-click-modal="false" :close-on-press-escape="false">
            <div class="ctpc-list clearfix">
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
                    <!--<span style="position: absolute;right: 10px;">-->
                    <!--<el-button type="text" icon="edit" @click="modifyStep(index,planTask)"></el-button>-->
                    <!--<el-button type="text" icon="close" @click="deleteTaskMember(index)"></el-button>-->
                    <!--</span>-->
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
                activeName:'new',
                beginTime:'',
                endTime:'',
                priority:-1,
                prioritys:[
                    {id:-1,name:'全部'},
                    {id:0,name:'普通'},
                    {id:1,name:'紧急'},
                    {id:2,name:'非常紧急'}
                ],
                type:-1,
                types:[
                    {id:-1,name:'全部'},
                    {id:0,name:'个人建议'},
                    {id:1,name:'市场反馈'},
                    {id:2,name:'公司决策'},
                ],
                readStatus:-1,
                readStatuses:[
                    {id:-1,name:'全部'},
                    {id:0,name:'未读'},
                    {id:1,name:'已读'},
                ],
                origin:'',
                originList:[],
                introducer:'',
                introducerList:[],
                chargeMan:'',
                chargeManList:[],
                demandData:[],
                planTask:[],
                linkTaskId:'',
                taskData:[],
                taskStep:{
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
                projectList:[],
                priorityList:[],
                reqDTO:{
                    pageNum:1,
                    /*priority:null,
                    readStatus:null,
                    user:null,
                    chargeMan:null,
                    origin:null,
                    type:null,
                    beginTime:null,
                    endTime:null*/
                },
                demandPage:{
                    pageSize:10,
                    total:0,
                    currentPage:1
                },
                showTaskDetail:false,
                planVisible:false,
                isSaving:false,
                newDemandVisible:false,
                editDemandVisible:false,
                demandDeleteIcon:false,
                linkTaskVisible:false,
                demandForm:{
                    id:null,
                    title:null,
                    type:null,
                    priority:null,
                    origin:null,
                    question:null,
                    target:null,
                    releaseTime:null,
                    content:null,
                    projectId:'',
                    status:''
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
                        {required: true, validator: validateEmpty, message: '需求来源不能为空', trigger: 'blur'},
                    ],
                    title: [
                        {required: true, validator: validateEmpty, message: '需求名称不能为空', trigger: 'blur'},
                        {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
                    ],
                    type: [
                        {required: true,validator: validateEmpty, message: '类型不能空', trigger:'blur'}
                    ],
                    priority:[
                        {required: true,validator: validateEmpty, message: '优先级不能空', trigger:'blur'}
                    ],
                    question:[
                        {required: true,validator: validateEmpty, message: '问题不能为空', trigger:'blur'}
                    ],
                    target:[
                        {required: true,validator: validateEmpty, message: '目标不能为空', trigger:'blur'}
                    ],
                    releaseTime:[
                        {required: true,validator: validateEmpty, message: '期待上线时间不能为空', trigger:'blur'}
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
        },
        beforeMount:function () {
            this.fetchIntroducerList()
            this.fetchOriginList()
            this.fetchNewDemandList()
            this.fetchUserList()
            this.fetchProjectList()
            this.fetchStageList()


        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            getUserName(){
                let userName = helper.decodeToken().userName;
                return userName;
            }
        },
        methods:{

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
                http.zsyPostHttp('/feedback/demand/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.total
                })
            },

            //查询进行中需求
            fetchRunningDemandList(){
                http.zsyPostHttp('/feedback/demand-running/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.total
                })
            },

            //查询排队中需求
            fetchQueueDemandList(){
                http.zsyPostHttp('/feedback/demand-queued/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.total
                })
            },

            //查询不采纳需求
            fetchRejectedDemandList(){
                http.zsyPostHttp('/feedback/demand-rejected/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.total
                })
            },

            //查询已完成需求
            fetchCompletedDemandList(){
                http.zsyPostHttp('/feedback/demand-completed/list', this.reqDTO, (res) => {
                    this.demandData = res.data.list;
                    this.demandPage.total = res.data.total
                })
            },

            //查询我参与的需求
            fetchJoinedDemandList(){
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
                for(var i =0;i< this.adminList.length;i++){
                    if(this.taskStep.createBy == this.adminList[i].id){
                        this.taskStep.userName = this.adminList[i].name
                    }
                }


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
                this.planVisible=true
                this.feedbackPlanForm.feedbackId = feedback.id
                this.feedbackPlanForm.projectId = feedback.projectId
                this.demandForm.projectId =feedback.projectId
                this.demandForm.priority =feedback.priority
                this.demandForm.status = feedback.status
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

            //按要求查询需求
            select(num){
                this.reqDTO.pageNum = 1
                /*priority:null,
                    readStatus:null,
                    user:null,
                    chargeMan:null,
                    origin:null,
                    type:null,
                    beginTime:null,
                    endTime:null*/
                this.reqDTO.priority = undefined;
                this.reqDTO.readStatus = undefined;
                this.reqDTO.user = undefined;
                this.reqDTO.chargeMan = undefined;
                this.reqDTO.origin = undefined;
                this.reqDTO.type = undefined;
                this.reqDTO.beginTime = undefined;
                this.reqDTO.endTime = undefined;
                if (this.priority != undefined && this.priority != null){
                    this.reqDTO.priority = this.priority
                }
                if(this.readStatus != undefined && this.priority != null){
                    this.reqDTO.readStatus = this.readStatus
                }
                if(this.introducer){
                    this.reqDTO.user = String(this.introducer)
                }
                if (this.chargeMan){
                    this.reqDTO.chargeMan = String(this.chargeMan)
                }
                if(this.origin){
                    this.reqDTO.origin = this.origin
                }
                if (this.type != undefined && this.priority != null){
                    this.reqDTO.type = this.type
                }
                if (this.beginTime){
                    this.reqDTO.beginTime = this.beginTime
                }
                if (this.endTime){
                    this.reqDTO.endTime = this.endTime
                }
                //当num为0  按要求查询新需求列表
                if (num == 0){
                    this.fetchNewDemandList()
                }
                //当num为1  按要求查询进行中需求列表
                if (num == 1){
                    this.fetchRunningDemandList()
                }
                //当num为2  按要求查询排队中需求列表
                if (num == 4){
                    this.fetchQueueDemandList()
                }
                //当num为3  按要求查询不采纳需求列表
                if (num == 3){
                    this.fetchRejectedDemandList()
                }
                //当num为4  按要求查询已完成需求列表
                if (num == 2){
                    this.fetchCompletedDemandList()
                }

            },

            //添加任务
            addTask(){
                this.showTaskDetail = true
            },

            //跳转到需求详情页
            demandDetail(id){
                console.log(id)
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

            //添加需求
            saveDemand(formName){
                this.isSaving = true
                this.$refs[formName].validate((valid) => {
                    if (valid){
                        var param = this.demandForm
                        param.releaseTime = moment(param.releaseTime).format('YYYY-MM-DD HH:00:00')
                        if (this.demandForm.id != ''){
                            http.zsyPutHttp('/feedback/demand/edit/'+String(this.demandForm.id),param,(res) => {
                                this.$message({ showClose: true,message: '需求修改成功',type: 'success'});
                                this.$refs[formName].resetFields();
                                this.editDemandVisible = false
                                this.fetchNewDemandList();
                                this.isSaving =false
                                this.demandDeleteIcon = false
                            })
                        }else {
                            http.zsyPostHttp('/feedback/demand/add', param, (resp) => {
                                this.$message({ showClose: true,message: '需求创建成功',type: 'success'});
                                this.$refs[formName].resetFields();
                                this.newDemandVisible = false
                                this.fetchNewDemandList();
                                this.isSaving =false
                            });
                        }
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
                }else {
                    this.demandDetail(demand.id)
                    this.clearDemandForm()
                }
            },

            //删除需求
            deleteDemand(){
                this.$confirm('此操作将删除该需求, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp('/feedback/delete/'+this.demandForm.id, null, (resp) => {
                        this.$message({ showClose: true,message: '需求删除成功',type: 'success'});
                        this.feedbackVisible = false
                        this.fetchRunningDemandList();
                    });
                }).catch(() => {
                });
            },

            //清空需求表单
            clearDemandForm(){
                this.demandForm.id = null
                this.demandForm.title = null
                this.demandForm.type = null
                this.demandForm.priority= null
                this.demandForm.origin= null
                this.demandForm.question = null
                this.demandForm.target = null
                this.demandForm.releaseTime = null
                this.demandForm.content= null
            },

            handleCurrentChange(currentPage){
                this.reqDTO.pageNum = currentPage;
                this.fetchNewDemandList()

            },
            handleCurrentChange1(currentPage){
                this.reqDTO.pageNum = currentPage;
                this.fetchRunningDemandList()

            },
            handleCurrentChange2(currentPage){
                this.reqDTO.pageNum = currentPage;
                this.fetchCompletedDemandList()

            },
            handleCurrentChange3(currentPage){
                this.reqDTO.pageNum = currentPage;
                this.fetchRejectedDemandList()

            },
            handleCurrentChange4(currentPage){
                this.reqDTO.pageNum = currentPage;
                this.fetchQueueDemandList()

            },
            handleCurrentChange5(currentPage){
                this.reqDTO.pageNum = currentPage;
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
                    this.fetchNewDemandList()
                }else if(tab.name =="running"){
                    this.fetchRunningDemandList()
                }else if (tab.name=='queue'){
                    this.fetchQueueDemandList()
                }else if (tab.name == 'reject'){
                    this.fetchRejectedDemandList()
                } else  if (tab.name == 'completed'){
                    this.fetchCompletedDemandList()
                } else if (tab.name == 'joined'){
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
