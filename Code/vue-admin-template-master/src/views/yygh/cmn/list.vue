<template>
    <div class="app-container">
        <el-table :data="list" style="width: 100%" row-key="id" border lazy :load="load"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
            <el-table-column prop="name" label="名称" width="280">
            </el-table-column>
            <el-table-column prop="dictCode" label="编码" width="180">
            </el-table-column>
            <el-table-column prop="value" label="值" width="180">
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180">
            </el-table-column>
        </el-table>
        <div class="el-toolbar">
            <div class="el-toolbar-body" style="justify-content: flex-start;">
    	        <el-button type="text" @click="exportData"><i class="fa fa-plus"/> 导出</el-button>
                <el-button type="text" @click="importData"><i class="fa fa-plus"/> 导入</el-button>
            </div>
        </div>
        <el-dialog title="导入" :visible.sync="dialogImportVisible" width="480px">
            <el-form label-position="right" label-width="170px">
                <el-form-item label="文件">
                    <el-upload
                            :multiple="false"
                            :on-success="onUploadSuccess"
                            :action="'http://localhost:9001/admin/cmn/upload'"
                            class="upload-demo">
                        <el-button size="small" type="primary">点击上传</el-button>
                        <div slot="tip" class="el-upload__tip">只能上传xls文件，且不超过500kb</div>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogImportVisible = false">取消</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import dict from '@/api/dict.js'

export default {
    data() {
        return {
            list:[],
            pid:1,
            dialogImportVisible:false,
        }
    },
    methods: {
        load(tree, treeNode, resolve) {
            dict.getChildListByPid(tree.id).then(resp => {
                resolve(resp.data.items);
            })
        },
        exportData() {
            window.open("http://localhost:9001/admin/cmn/download");
        },
        importData() {
            this.dialogImportVisible = true;
        },
        onUploadSuccess() {
            this.$message.success('上传成功');
            dict.getChildListByPid(1).then(resp => {
                this.list=resp.data.items;
            })
            this.dialogImportVisible = false;
        }
    },
    created() {
        dict.getChildListByPid(1).then(resp => {
            this.list=resp.data.items;
        })
        
    }
}


</script>