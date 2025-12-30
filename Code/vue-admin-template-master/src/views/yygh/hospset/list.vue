<template>
    <div class="app-container">
        <el-form :inline="true" :model="searchObj" class="demo-form-inline">
            <el-form-item label="医院名称">
                <el-input v-model="searchObj.hosname" placeholder="医院名称"></el-input>
            </el-form-item>
            <el-form-item label="医院编号">
                <el-input v-model="searchObj.hoscode" placeholder="医院编号"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="query()">查询</el-button>
                <el-button type="default" @click="clearData()">清空</el-button>
            </el-form-item>
        </el-form>
        
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="batchDel()">删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="listLoading" :data="list" element-loading-text="数据加载中" border fit highlight-current-row @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"/>
            <el-table-column label="序号" width="40" align="center">
                <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column prop="id" label="id" width="40" />
            <el-table-column prop="hosname" label="医院名称" width="180" />
            <el-table-column prop="hoscode" label="医院编号" width="80" />
            <el-table-column prop="apiUrl" label="api地址" width="100" />
            <el-table-column prop="contactsName" label="联系人" width="140" />
            <el-table-column prop="contactsPhone" label="联系电话" width="100" />
            <el-table-column prop="status" label="状态" width="70">
                <template slot-scope="scope">
                    {{ scope.row.status === 1 ? '可用' : '不可用' }}
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="160" />
            <el-table-column prop="updateTime" label="更新时间" width="160" />
            <el-table-column label="操作" width="280" align="center">
                <template slot-scope="scope">
                    <router-link :to="'/yygh/hospset/edit/' + scope.row.id">
                        <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
                    </router-link>
                    <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteHospSetById(scope.row.id)">删除</el-button>
                    <el-button v-if = "scope.row.status==1" type="success" size="mini" icon="el-icon-lock" @click="lockHospSetById(scope.row.id,0)">锁定</el-button>
                    <el-button v-if = "scope.row.status==0" type="success" size="mini" icon="el-icon-unlock" @click="lockHospSetById(scope.row.id,1)">解锁</el-button>

                </template>
            </el-table-column>
        </el-table>
        <el-pagination @current-change="handleCurrentChange" :current-page=page :page-size=limit
            layout="total, prev, pager, next, jumper" :total=total>
        </el-pagination>
    </div>
</template>
<script>
import hospset from '@/api/hospset.js'
import { ConsoleWriter } from 'istanbul-lib-report';
export default {
    data() {
        return {
            listLoading: true, // 是否显示loading信息
            list: [],// 数据列表
            total: 0, // 总记录数
            page: 1, // 页码
            limit: 5, // 每页记录数
            searchObj: {},// 查询条件
            multipleSelection:[] //被选中行数据信息
        }
    },
    methods: {
        handleCurrentChange(val) {
            this.getHospSetPage(val);
        },
        getHospSetPage(val=1){
            this.page = val;
            hospset.getHospSetPage(this.page, this.limit, this.searchObj).then(resp => {
                this.total = resp.data.total;
                this.list = resp.data.rows;
                this.listLoading = false;
            })
        },
        query() {
            this.getHospSetPage();
        },
        clearData(){
            this.searchObj = {};
            this.getHospSetPage();
        },
        deleteHospSetById(id) {
            this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                hospset.deleteHospSetById(id).then(resp => {
                    this.$message.success(resp.message);
                    this.getHospSetPage();
                })
            }).catch((resp) => {
                if (resp == "cancel") {
                    this.$message.info ('已取消删除');
                } else {
                    this.$message.error('删除失败');
                }
            });
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        batchDel() {
            this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var ids = [];
                for(let selected of this.multipleSelection) {
                    ids.push(selected.id);
                };
                hospset.deleteHospSetByIds(ids).then(resp=>{
                    this.$message.success('批量删除成功');
                    this.getHospSetPage();
                })
                
            }).catch((resp) => {
                if (resp == "cancel") {
                    this.$message.info ('已取消删除');
                } else {
                    this.$message.error('删除失败');
                }
            });
        },
        lockHospSetById(id,status) {
           hospset.lockHospSetById(id,status).then(resp=>{
            this.getHospSetPage();
           })
        }
    },
    created() {
       this.getHospSetPage();
    }
}
</script>