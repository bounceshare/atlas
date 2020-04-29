<#include "/header.ftl">
<#include "/scripts/utils.js">

<style>

        .g-signin2{
          width: 100%;
        }

        .g-signin2 > div{
          margin: 0 auto;ip
        }

</style>

<body>
<div class="container">
    <div class="row">
        <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
            <div class="card card-signin my-5">
                <div class="card-body">
                    <div class="text-center">
                        <img src="${logo}" width="200" height="200" alt="bounce.png"></img>
                    </div>
                    <br/>
                    <h5 class="card-title text-center">Atlas</h5>
                    <form class="form-signin">
                        <!--<button class="btn btn-lg btn-google btn-block text-uppercase g-signin2" data-onsuccess="onSignIn" type="submit"><i-->
                                <!--class="fab fa-google mr-2"></i> Sign in with Google-->
                        <!--</button>-->
                    <div class="g-signin2" data-onsuccess="onSignIn" data-width="300" data-height="50" data-longtitle="true">Sign in with google</div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>