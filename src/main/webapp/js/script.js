$.fn.datepicker.defaults.format = "yyyy-mm-dd";

$(document).ready(function() {
    $('#datepicker').datepicker({
            format: 'yyyy-mm-dd',
            startDate: '1900-01-01',
            endDate: '+0'
        }).on('changeDate', function(e) {
            $("#selectedDate").val($("#datepicker").datepicker('getFormattedDate'));

    });


});

