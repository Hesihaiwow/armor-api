<template>
</template>
<script>
    import http from '../lib/Http'
    import moment from 'moment';
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElDialog from "../../node_modules/element-ui/packages/dialog/src/component";
    import helper from '../lib/Helper'


    moment.locale('zh-cn');
    export default {
        components: {
            ElDialog,
            ElButton},
        name: 'Plan',
        data() {
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
            }
        },
    }
</script>

