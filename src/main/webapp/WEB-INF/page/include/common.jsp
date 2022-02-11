<script type="text/javascript">
    function validform() {
        /*关键在此增加了一个return，返回的是一个validate对象，这个对象有一个form方法，返回的是是否通过验证*/
        return $(".inputform").validate();
    }

    $(function () {
        validform();
    });
</script>