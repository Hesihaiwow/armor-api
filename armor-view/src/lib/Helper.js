export default {
    //去空格
    trim: function( text ) {
        let rTrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
        return text == null ? "" : ( text + "" ).replace( rTrim, "" );
    },
    //解Token
    decodeToken: function () {
        let token = window.localStorage.getItem("token");
        if (token==null||token=='') {
            var tmp = {};
            tmp.userName = null;
            tmp.userId = null;
            tmp.userRole = null;
            tmp.departmentId = null;
            tmp.orgId = null;
            tmp.isAdmin = null;
            return tmp;
        }
        let tokens = token.split(".");
        var output = tokens[1].replace(/-/g, '+').replace(/_/g, '/');
        switch (output.length % 4) {
            case 0:
                break;
            case 2:
                output += '==';
                break;
            case 3:
                output += '=';
                break;
            default:
                throw 'Illegal base64url string!';
        }
        let Base64 = require('js-base64').Base64;
        return eval('('+Base64.decode(output)+')');
    }
    
}