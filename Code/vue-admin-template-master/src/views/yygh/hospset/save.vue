<template>
    <div class="app-container">
        <el-form label-width="120px" :model="hospset" :rules="rules" ref="ruleForm">
            <el-form-item label="医院名称" prop="hosname">
                <el-input v-model="hospset.hosname" />
            </el-form-item>
            <el-form-item label="医院编号" prop="hoscode">
                <el-input v-model="hospset.hoscode" />
            </el-form-item>
            <el-form-item label="api地址">
                <el-input v-model="hospset.apiUrl" />
            </el-form-item>
            <el-form-item label="联系人">
                <el-input v-model="hospset.contactsName" />
            </el-form-item>
            <el-form-item label="联系电话">
                <el-input v-model="hospset.contactsPhone" />
            </el-form-item>
            <el-form-item>
                <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate('ruleForm')">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import hospset from '@/api/hospset.js'
    export default {
        data(){
            return {
                saveBtnDisabled:false,
                hospset:{

                },
                rules: {
                    hosname: [
                        { required: true, message: '请输入医院名称', trigger: 'blur' },
                        { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
                    ],
                    hoscode: [
                        { required: true, message: '请输入医院编号', trigger: 'blur' },
                        { reqtype: 'number', min: 5, max: 5, message: '长度为 5 位数字', trigger: 'blur' }
                    ]
                }
            }
            
        },                          
        methods: {
            saveOrUpdate(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.saveBtnDisabled=true;
                        if(!this.hospset.id) { //新增
                            hospset.saveHospSet(this.hospset).then(resp=>{
                                this.$message.success("添加成功");
                                this.$router.push({path: '/yygh/hospset/list'})
                            }).catch(resp=>{
                                this.$message.error("添加失败");
                            })
                        } else {
                            hospset.updateHospSetById(this.hospset).then(resp=>{
                                this.$message.success("修改成功");
                                this.$router.push({path: '/yygh/hospset/list'})
                            }).catch(resp=>{
                                this.$message.error("修改失败");
                            })
                        }
                    } else {
                        this.$message.error('表单填写有误!');
                        return false;
                    }
                });
                
            }
        },
        created() {
            if(this.$route.params && this.$route.params.id) {
                var id = this.$route.params.id;
                hospset.queryHospSetById(id).then(resp=>{
                    this.hospset = resp.data.item
                }).catch(resp=> {
                    this.$message.error(resp.message);
                })
            }
        }
    }
</script>